package iberoplast.pe.gespro.ui.modules.user

import Role
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
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

class FormUserActivity : AppCompatActivity() {
    private val apiService by lazy {
        ApiService.create()
    }
    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }
    private val etName by lazy { findViewById<EditText>(R.id.etName) }
    private val etEmail by lazy { findViewById<EditText>(R.id.etEmail) }
    private val spRole by lazy { findViewById<Spinner>(R.id.spRole) }
    private val btnFormUser by lazy { findViewById<Button>(R.id.btnFormUser) }
    private val btnGoToList by lazy { findViewById<Button>(R.id.btnGoToList) }
    private var roles: List<Role> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_user)

        val isEditing = intent.getBooleanExtra("isEditing", false)
        val user = intent.getParcelableExtra<User>("user")

        // Si isEditing es true, estás editando un proveedor, muestra los datos existentes
        if (isEditing && user != null) {
            ActionBarUtils.setCustomTitle(
                this,
                "Editando usuario:",
                "${user.name}"
            )
            etName.setText(user.name)
            etEmail.setText(user.email)
            btnFormUser.text = "Actualizar"
            btnFormUser.setOnClickListener {
                val id = user.id
                executeMethodUpdate(id)
            }
        } else {
            ActionBarUtils.setCustomTitle(
                this,
                "Registro de nuevo usuario"
            )
            btnFormUser.text = "Crear"
            btnFormUser.setOnClickListener {
                executeMethodCreate()
            }
        }
        btnGoToList.setOnClickListener {
            val intent = Intent(this, UserActivity::class.java)
            startActivity(intent)
            finish()
        }
        loadRoles(spRole, user)
    }
    private fun loadRoles(spRole: Spinner, user: User?) {
        val jwt = preferences["jwt", ""]
        val call = apiService.getRoles("Bearer $jwt")
        call.enqueue(object : Callback<ArrayList<Role>> {
            override fun onResponse(
                call: Call<ArrayList<Role>>,
                response: Response<ArrayList<Role>>
            ) {
                if (response.isSuccessful) {
                    roles = response.body() ?: ArrayList()

                    // Crear un ArrayAdapter solo con los nombres de los roles
                    val adapter = ArrayAdapter(
                        this@FormUserActivity,
                        android.R.layout.simple_spinner_item,
                        roles.map { it.name }
                    )

                    // Establecer el estilo del Spinner
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    // Asignar el ArrayAdapter al Spinner
                    spRole.adapter = adapter

                    // Seleccionar el rol asociado al usuario (si existe)
                    user?.role?.let { selectedRole ->
                        val selectedIndex = roles.indexOfFirst { it.id == selectedRole.id }
                        if (selectedIndex != -1) {
                            spRole.setSelection(selectedIndex)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<Role>>, t: Throwable) {
                // Manejo de errores en caso de fallo en la solicitud
                t.localizedMessage?.let { toast(it) }
            }
        })
    }


    private fun executeMethodCreate() {
        val jwt = preferences["jwt", ""]
        val name = findViewById<EditText>(R.id.etName).text.toString()
        val email = findViewById<EditText>(R.id.etEmail).text.toString()

        val selectedRoleName = spRole.selectedItem as? String
        val selectedRole = roles.firstOrNull { it.name == selectedRoleName }

        // Obtener el ID del rol seleccionado
        val id_role = selectedRole?.id ?: -1

        val call = apiService.postUser("Bearer $jwt", name, email, id_role)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(
                call: Call<Void>,
                response: Response<Void>
            ) {
                if (response.isSuccessful) {
                    val intent = Intent(this@FormUserActivity, UserActivity::class.java)
                    toast("Usuario creado correctamente")
                    startActivity(intent)
                    finish()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Manejar errores de la solicitud
            }
        })
    }


    private fun executeMethodUpdate(id: Int) {
        val jwt = preferences["jwt", ""]
        val name = findViewById<EditText>(R.id.etName).text.toString()
        val email = findViewById<EditText>(R.id.etEmail).text.toString()

        val selectedRoleName = spRole.selectedItem as? String
        val selectedRole = roles.firstOrNull { it.name == selectedRoleName }

        // Obtener el ID del rol seleccionado
        val id_role = selectedRole?.id ?: -1 // Cambia esto según el valor por defecto

        val call = apiService.updateUser("Bearer $jwt", id, name, email, id_role)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    toast("Usuario actualizado correctamente")
                    val intent = Intent(this@FormUserActivity, UserActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Manejar errores de la solicitud
            }
        })
    }
}