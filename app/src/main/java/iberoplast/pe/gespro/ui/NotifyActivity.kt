package iberoplast.pe.gespro.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
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

class NotifyActivity : AppCompatActivity() {

    private val apiService: ApiService by lazy {
        ApiService.create()
    }
    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }

    private val etTitle by lazy { findViewById<EditText>(R.id.etTitle) }
    private val etBody by lazy { findViewById<EditText>(R.id.etBody) }
    private val btnNotify by lazy { findViewById<Button>(R.id.btnNotify) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notify)

        ActionBarUtils.setCustomTitle(
            this,
            "Notificar",
            "Todos los usuarios"
        )

        val appName = getString(R.string.app_name)
        etTitle.setText(appName)

        // Enviar los datos a Laravel (aquí debes implementar tu lógica de envío)
        btnNotify.setOnClickListener {
            sendNotify()
        }
    }

    private fun sendNotify() {
        val title = etTitle.text.toString().trim()
        val body = etBody.text.toString().trim()

        if (isValidInput(title, etTitle) && isValidInput(body, etBody)) {
            val jwt = preferences["jwt", ""]
            val authHeader = "Bearer $jwt"
            val call = apiService.sendNotification(authHeader, title, body)

            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        toast("Se ha enviado la notificación correctamente")
                        val intent = Intent(this@NotifyActivity, MenuActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    toast("Error en la solicitud: ${t.message}")
                }
            })
        }
    }
    private fun isValidInput(input: String, editText: EditText): Boolean {
        if (input.isEmpty()) {
            editText.error = "Este campo no puede estar vacío"
            return false
        }

        return true
    }
}