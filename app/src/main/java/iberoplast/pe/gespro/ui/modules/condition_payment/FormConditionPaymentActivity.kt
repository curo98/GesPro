package iberoplast.pe.gespro.ui.modules.condition_payment

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import iberoplast.pe.gespro.R
import iberoplast.pe.gespro.io.ApiService
import iberoplast.pe.gespro.model.TypePayment
import iberoplast.pe.gespro.util.ActionBarUtils
import iberoplast.pe.gespro.util.PreferenceHelper
import iberoplast.pe.gespro.util.PreferenceHelper.get
import iberoplast.pe.gespro.util.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormConditionPaymentActivity : AppCompatActivity() {
    private val apiService by lazy {
        ApiService.create()
    }
    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }
    private val etNameCondition by lazy { findViewById<EditText>(R.id.etNameCondition) }
    private val etConditionDescription by lazy { findViewById<EditText>(R.id.etConditionDescription) }
    private val btnFormCondition by lazy { findViewById<Button>(R.id.btnFormCondition) }
    private val btnGoToList by lazy { findViewById<Button>(R.id.btnGoToList) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_condition_payment)

        val isEditing = intent.getBooleanExtra("isEditing", false)
        val condition_payment = intent.getParcelableExtra<TypePayment>("condition_payment")

        // Si isEditing es true, estás editando un proveedor, muestra los datos existentes
        if (isEditing && condition_payment != null) {
            ActionBarUtils.setCustomTitle(
                this,
                "Editando condicion de pago:",
                "${condition_payment.name}"
            )
            etNameCondition.setText(condition_payment.name)
            etConditionDescription.setText(condition_payment.description)
            btnFormCondition.text = "Actualizar"
            btnFormCondition.setOnClickListener {
                val id = condition_payment.id
                executeMethodUpdate(id)
            }
        } else {
            ActionBarUtils.setCustomTitle(
                this,
                "Registro de nueva condicion de pago"
            )
            btnFormCondition.text = "Crear"
            btnFormCondition.setOnClickListener {
                executeMethodCreate()
            }
        }
        btnGoToList.setOnClickListener {
            val intent = Intent(this, ConditionPaymentActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun executeMethodCreate() {
        val jwt = preferences["jwt", ""]
        val name = findViewById<EditText>(R.id.etNameCondition).text.toString()
        val description = findViewById<EditText>(R.id.etConditionDescription).text.toString()

        val call = apiService.postConditionPayment("Bearer $jwt", name, description)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(
                call: Call<Void>,
                response: Response<Void>
            ) {
                if (response.isSuccessful) {
                    val intent = Intent(this@FormConditionPaymentActivity, ConditionPaymentActivity::class.java)
                    toast("Condicion de pago creado correctamente")
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
        val description = findViewById<EditText>(R.id.etConditionDescription).text.toString()
        val name = findViewById<EditText>(R.id.etNameCondition).text.toString()

        val call = apiService.updateConditionPayment("Bearer $jwt", id, name, description)
        call.enqueue(object: Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    toast("Condicion de pago actualizado correctamente")
                    val intent = Intent(this@FormConditionPaymentActivity, ConditionPaymentActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
            }
        })
    }
}