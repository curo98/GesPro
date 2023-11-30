package iberoplast.pe.gespro.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import iberoplast.pe.gespro.R
import iberoplast.pe.gespro.io.ApiService
import iberoplast.pe.gespro.model.User
import iberoplast.pe.gespro.util.ActionBarUtils
import iberoplast.pe.gespro.util.PreferenceHelper
import iberoplast.pe.gespro.util.PreferenceHelper.get
import iberoplast.pe.gespro.util.toast
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

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

    private fun displayProfileData(user: User){
        // Obtener el TextInputLayout con el id iln
        val iln = findViewById<TextInputLayout>(R.id.iln)
        // Obtener el TextInputEditText con el id etName
        val etName = iln.editText
        // Establecer el texto del TextInputEditText
        etName?.setText(user.name)

        val ile = findViewById<TextInputLayout>(R.id.ile)
        // Obtener el TextInputEditText con el id etName
        val etEmail = ile.editText
        // Establecer el texto del TextInputEditText
        etEmail?.setText(user.email)

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

        val etName = findViewById<TextInputEditText>(R.id.etName)
        val etEmail = findViewById<TextInputEditText>(R.id.etEmail)

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