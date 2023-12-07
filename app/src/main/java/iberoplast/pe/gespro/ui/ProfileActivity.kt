package iberoplast.pe.gespro.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.squareup.picasso.Picasso
import iberoplast.pe.gespro.R
import iberoplast.pe.gespro.io.ApiService
import iberoplast.pe.gespro.model.User
import iberoplast.pe.gespro.util.ActionBarUtils
import iberoplast.pe.gespro.util.PreferenceHelper
import iberoplast.pe.gespro.util.PreferenceHelper.get
import iberoplast.pe.gespro.util.toast
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {
    private val apiService: ApiService by lazy {
        ApiService.create()
    }
    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }

    private val ciPhotoProfile by lazy { findViewById<ImageView>(R.id.ciPhotoProfile) }
    private val etName by lazy { findViewById<EditText>(R.id.etName) }
    private val etEmail by lazy { findViewById<EditText>(R.id.etEmail) }

    private lateinit var getContent: ActivityResultLauncher<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Inicializar getContent
        getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { selectedUri ->
                val fileName = getFileName(selectedUri)
                val fileContent = getContentBytes(selectedUri)
                uploadPhotoProfile(fileName, fileContent)
            }
        }

        ciPhotoProfile.setOnClickListener {
            openGallery(getContent)
        }

        val UserName = preferences["UserName", ""]

        ActionBarUtils.setCustomTitle(
            this,
            "Mi perfil",
            "${UserName}"
        )

        val jwt = preferences["jwt", ""]
        val authHeader = "Bearer $jwt"
        val call = apiService.getUser(authHeader)

        val llLoader = findViewById<LinearLayout>(R.id.llLoader)
        val cvProfile = findViewById<CardView>(R.id.cvProfile)

        cvProfile.visibility = View.GONE
        llLoader.visibility = View.VISIBLE


        call.enqueue(object: Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful){
                    val user = response.body()
                    if (user != null) {
                        displayProfileData(user)
                    }
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                t.localizedMessage?.let { toast(it) }
            }

        })
    }

    private fun openGallery(getContent: ActivityResultLauncher<String>) {
        // Lanzar la actividad de selección de archivos
        getContent.launch("image/*")
    }
    private fun getFileName(uri: Uri): String {
        // Obtener el nombre del archivo desde su URI
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val displayNameColumnIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (displayNameColumnIndex != -1) {
                    val displayName = it.getString(displayNameColumnIndex)
                    if (displayName != null) {
                        return displayName
                    }
                }
            }
        }
        return "Archivo no encontrado"
    }
    private fun getContentBytes(uri: Uri): ByteArray {
        // Obtener los bytes del contenido del archivo
        val inputStream = contentResolver.openInputStream(uri)
        return inputStream?.readBytes() ?: byteArrayOf()
    }
    private fun uploadPhotoProfile(filename: String, fileContent: ByteArray) {
        val jwt = preferences["jwt", ""]
        val authHeader = "Bearer $jwt"
        // Crear el objeto de la solicitud multipart
        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), fileContent)
        val file = MultipartBody.Part.createFormData("file", filename, requestFile)

        // Realizar la llamada a la API para cargar la foto
        val call: Call<Void> = apiService.uploadPhotoProfile(authHeader, file)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // Éxito en la carga
                    toast("Foto de perfil actualizada")
                    val intent =
                        Intent(this@ProfileActivity, ProfileActivity::class.java)
                    finish()
                    startActivity(intent)
                } else {
                    toast("Error al cargar la foto")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                toast("Error en la llamada de carga")
            }
        })
    }

    private fun displayProfileData(user: User){
        val uri = user.photo
        // Concatenar el dominio con la URI para formar la URL completa
        val domain = "https://gespro-iberoplast.tech"
        val photoURL = "$domain$uri"
        Picasso.get()
            .load(photoURL)
            .placeholder(R.drawable.incognito) // Imagen de marcador de posición mientras carga
            .error(R.drawable.incognito) // Imagen de error si falla la carga
            .fit()
            .centerCrop()
            .into(ciPhotoProfile)

        etName.setText(user.name)
        etEmail.setText(user.email)

        val llLoader = findViewById<LinearLayout>(R.id.llLoader)
        val cvProfile = findViewById<CardView>(R.id.cvProfile)

        llLoader.visibility = View.GONE
        cvProfile.visibility = View.VISIBLE

        val btnUpdateProfile = findViewById<Button>(R.id.btnUpdateProfile)
        btnUpdateProfile.setOnClickListener {
            updateProfile()
        }
    }

    private fun updateProfile(){
        val name = etName.text.toString().trim() // Obtén el valor del nombre y elimina espacios en blanco iniciales y finales
        val email = etEmail.text.toString().trim() // Obtén el valor del correo electrónico y elimina espacios en blanco iniciales y finales

        if (name.isEmpty()) {
            // Campo de nombre vacío
            etName.setError("El nombre no puede estar vacío", null) // Muestra el mensaje de error en el EditText
            return
        } else {
            etName.setError(null, null) // Borra el mensaje de error si el campo es válido
        }

        if (email.isEmpty()) {
            // Campo de correo electrónico vacío
            etEmail.setError("El correo electrónico no puede estar vacío", null) // Muestra el mensaje de error en el EditText
            return
        } else {
            etEmail.setError(null, null) // Borra el mensaje de error si el campo es válido
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // Correo electrónico no válido
            etEmail.setError("Ingrese un correo electrónico válido", null) // Muestra el mensaje de error en el EditText
            return
        } else {
            etEmail.setError(null, null) // Borra el mensaje de error si el campo es válido
        }

        val jwt = preferences["jwt", ""]
        val authHeader = "Bearer $jwt"

        val call = apiService.postUser(authHeader, name, email)
        call.enqueue(object: Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful){
                    toast("Se ha actualizado los datos de tu perfil")
                    finish()
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                t.localizedMessage?.let { toast(it) }
            }

        })
    }
}