package iberoplast.pe.gespro.ui.modules.state

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import iberoplast.pe.gespro.R
import iberoplast.pe.gespro.io.ApiService
import iberoplast.pe.gespro.model.StateRequest
import iberoplast.pe.gespro.util.ActionBarUtils
import iberoplast.pe.gespro.util.PreferenceHelper
import iberoplast.pe.gespro.util.PreferenceHelper.get
import iberoplast.pe.gespro.util.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormStateRequestActivity : AppCompatActivity() {
    private val apiService by lazy {
        ApiService.create()
    }
    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }
    private val etStateName by lazy { findViewById<EditText>(R.id.etStateName) }
    private val etStateDescription by lazy { findViewById<EditText>(R.id.etStateDescription) }
    private val btnFormState by lazy { findViewById<Button>(R.id.btnFormState) }
    private val btnGoToList by lazy { findViewById<Button>(R.id.btnGoToList) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_state_request)

        val isEditing = intent.getBooleanExtra("isEditing", false)
        val state = intent.getParcelableExtra<StateRequest>("state")

        // Si isEditing es true, estás editando un proveedor, muestra los datos existentes
        if (isEditing && state != null) {
            ActionBarUtils.setCustomTitle(
                this,
                "Edición de estado:",
                "${state.name}"
            )
            etStateName.setText(state.name)
            etStateDescription.setText(state.description)
            btnFormState.text = "Actualizar"
            btnFormState.setOnClickListener {
                val id = state.id
                executeMethodUpdate(id)
            }
        } else {
            ActionBarUtils.setCustomTitle(
                this,
                "Registrar nuevo estado"
            )
            btnFormState.text = "Crear"
            btnFormState.setOnClickListener {
                executeMethodCreate()
            }
        }
        btnGoToList.setOnClickListener {
            val intent = Intent(this, StateRequestActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun executeMethodCreate() {
        val jwt = preferences["jwt", ""]
        val name = findViewById<EditText>(R.id.etStateName).text.toString()
        val description = findViewById<EditText>(R.id.etStateDescription).text.toString()

        val call = apiService.postState("Bearer $jwt", name, description)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(
                call: Call<Void>,
                response: Response<Void>
            ) {
                if (response.isSuccessful) {
                    val intent = Intent(this@FormStateRequestActivity, StateRequestActivity::class.java)
                    toast("Estado creado correctamente")
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
        val description = findViewById<EditText>(R.id.etStateDescription).text.toString()

        val call = apiService.updateState("Bearer $jwt", id, description)
        call.enqueue(object: Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    toast("Estado actualizado correctamente")
                    val intent = Intent(this@FormStateRequestActivity, StateRequestActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
            }
        })
    }
}