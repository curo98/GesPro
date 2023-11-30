package iberoplast.pe.gespro.ui.modules.role

import Role
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import iberoplast.pe.gespro.R
import iberoplast.pe.gespro.io.ApiService
import iberoplast.pe.gespro.util.ActionBarUtils
import iberoplast.pe.gespro.util.PreferenceHelper
import iberoplast.pe.gespro.util.PreferenceHelper.get
import iberoplast.pe.gespro.util.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormRoleActivity : AppCompatActivity() {
    private val apiService by lazy {
        ApiService.create()
    }
    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }
    private val etRoleName by lazy { findViewById<EditText>(R.id.etRoleName) }
    private val etRoleDescription by lazy { findViewById<EditText>(R.id.etRoleDescription) }
    private val btnFormRole by lazy { findViewById<Button>(R.id.btnFormRole) }
    private val btnGoToList by lazy { findViewById<Button>(R.id.btnGoToList) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_role)

        val isEditing = intent.getBooleanExtra("isEditing", false)
        val role = intent.getParcelableExtra<Role>("role")

        // Si isEditing es true, estás editando un proveedor, muestra los datos existentes
        if (isEditing && role != null) {
            ActionBarUtils.setCustomTitle(
                this,
                "Editando rol:",
                "${role.name}"
            )
            etRoleName.setText(role.name)
            etRoleDescription.setText(role.description)
            btnFormRole.text = "Actualizar"
            btnFormRole.setOnClickListener {
                val id = role.id
                executeMethodUpdate(id)
            }
        } else {
            ActionBarUtils.setCustomTitle(
                this,
                "Registro de nuevo rol"
            )
            btnFormRole.text = "Crear"
            btnFormRole.setOnClickListener {
                executeMethodCreate()
            }
        }
        btnGoToList.setOnClickListener {
            val intent = Intent(this, RoleActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun executeMethodCreate() {
        val jwt = preferences["jwt", ""]
        val name = findViewById<EditText>(R.id.etRoleName).text.toString()
        val description = findViewById<EditText>(R.id.etRoleDescription).text.toString()

        val call = apiService.postRole("Bearer $jwt", name, description)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(
                call: Call<Void>,
                response: Response<Void>
            ) {
                if (response.isSuccessful) {
                    val intent = Intent(this@FormRoleActivity, RoleActivity::class.java)
                    toast("Rol creado correctamente")
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
        val description = findViewById<EditText>(R.id.etRoleDescription).text.toString()

        val call = apiService.updateRole("Bearer $jwt", id, description)
        call.enqueue(object: Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    toast("Rol actualizado correctamente")
                    val intent = Intent(this@FormRoleActivity, RoleActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
            }
        })
    }
}