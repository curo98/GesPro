package iberoplast.pe.gespro.ui.modules.method_payment

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import iberoplast.pe.gespro.R
import iberoplast.pe.gespro.io.ApiService
import iberoplast.pe.gespro.model.MethodPayment
import iberoplast.pe.gespro.util.ActionBarUtils
import iberoplast.pe.gespro.util.PreferenceHelper
import iberoplast.pe.gespro.util.PreferenceHelper.get
import iberoplast.pe.gespro.util.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormMethodPaymentActivity : AppCompatActivity() {
    private val apiService by lazy {
        ApiService.create()
    }
    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }
    private val etNameMethod by lazy { findViewById<EditText>(R.id.etNameMethod) }
    private val etMethodDescription by lazy { findViewById<EditText>(R.id.etMethodDescription) }
    private val btnFormMethod by lazy { findViewById<Button>(R.id.btnFormMethod) }
    private val btnGoToList by lazy { findViewById<Button>(R.id.btnGoToList) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_method_payment)

        val isEditing = intent.getBooleanExtra("isEditing", false)
        val method_payment = intent.getParcelableExtra<MethodPayment>("method_payment")

        // Si isEditing es true, estás editando un proveedor, muestra los datos existentes
        if (isEditing && method_payment != null) {
            ActionBarUtils.setCustomTitle(
                this,
                "Editando metodo de pago:",
                "${method_payment.name}"
            )
            etNameMethod.setText(method_payment.name)
            etMethodDescription.setText(method_payment.description)
            btnFormMethod.text = "Actualizar"
            btnFormMethod.setOnClickListener {
                val id = method_payment.id
                executeMethodUpdate(id)
            }
        } else {
            ActionBarUtils.setCustomTitle(
                this,
                "Registro de nuevo metodo de pago"
            )
            btnFormMethod.text = "Crear"
            btnFormMethod.setOnClickListener {
                executeMethodCreate()
            }
        }
        btnGoToList.setOnClickListener {
            val intent = Intent(this, MethodPaymentActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun executeMethodCreate() {
        val jwt = preferences["jwt", ""]
        val name = findViewById<EditText>(R.id.etNameMethod).text.toString()
        val description = findViewById<EditText>(R.id.etMethodDescription).text.toString()

        val call = apiService.postMethodPayment("Bearer $jwt", name, description)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(
                call: Call<Void>,
                response: Response<Void>
            ) {
                if (response.isSuccessful) {
                    val intent = Intent(this@FormMethodPaymentActivity, MethodPaymentActivity::class.java)
                    toast("Metodo de pago creado correctamente")
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
        val description = findViewById<EditText>(R.id.etMethodDescription).text.toString()
        val name = findViewById<EditText>(R.id.etNameMethod).text.toString()

        val call = apiService.updateMethodPayment("Bearer $jwt", id, name, description)
        call.enqueue(object: Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    toast("Condicion de pago actualizado correctamente")
                    val intent = Intent(this@FormMethodPaymentActivity, MethodPaymentActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
            }
        })
    }
}