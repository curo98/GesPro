package iberoplast.pe.gespro

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class RegisterRequestSupplierActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_request_supplier)

        // Start code formstep1
        val btnNextFormRequest1 = findViewById<Button>(R.id.btnNextFormRequest1)
        val btnNextFormRequest2 = findViewById<Button>(R.id.btnNextFormRequest2)
        val btnNextFormRequest3 = findViewById<Button>(R.id.btnNextFormRequest3)
        val btnNextFormRequest4 = findViewById<Button>(R.id.btnNextFormRequest4)

        btnNextFormRequest1.setOnClickListener{
            val etNicRuc = findViewById<EditText>(R.id.etNicRuc)
            val nicRucText = etNicRuc.text.toString().trim()

            val llFormStep1 = findViewById<LinearLayout>(R.id.llFormStep1)
            val llFormStep2 = findViewById<LinearLayout>(R.id.llFormStep2)

            if (nicRucText.isEmpty()) {
                etNicRuc.error = "El NIC o RUC no puede estar vacío"
            } else if (!nicRucText.matches(Regex("^[0-9]+$"))) {
                etNicRuc.error = "Solo se permiten números en el NIC o RUC"
            } else if (nicRucText.length < 10) {
                etNicRuc.error = "El NIC o RUC es demasiado corto"
            } else {
                // Continuar con el paso 2(TEST)
                llFormStep1.visibility = View.GONE
                llFormStep2.visibility = View.VISIBLE
//                val intent = Intent(this, RequestSupplierStep2Activity::class.java)
//                startActivity(intent)
            }
        }
        // end code formstep1

        // Start code formstep2
        val spPaises = findViewById<Spinner>(R.id.spinnerPaises)
        val spDepartamentos = findViewById<Spinner>(R.id.spinnerDepartamentos)
        val spDistritos = findViewById<Spinner>(R.id.spinnerDistritos)

        val paises = arrayOf("Peru", "Colombia", "Chile", "Venezuela")
        val departamentos = arrayOf("Lima", "Iquitos", "Ica", "Puno")
        val distritos = arrayOf("San vicente de canete", "La victoria", "Pucusana", "Juliaca")

        spPaises.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, paises)
        spDepartamentos.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, departamentos)
        spDistritos.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, distritos)

        btnNextFormRequest2.setOnClickListener{
            val llFormStep2 = findViewById<LinearLayout>(R.id.llFormStep2)
            val llFormStep3 = findViewById<LinearLayout>(R.id.llFormStep3)
            // Continuar con el paso 2(TEST)
            llFormStep2.visibility = View.GONE
            llFormStep3.visibility = View.VISIBLE
        }
        // end code formstep2

        // Start code formstep3
        val webView = findViewById<WebView>(R.id.webView)

        val url = "https://drive.google.com/file/d/1Y4gwaZnYgfpS2Y2eE6CtMEnMMapuA5aA/view?usp=drive_link"
        // Reemplaza "ENLACE_PDF" con el enlace de Google Drive a tu archivo PDF

        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        webView.loadUrl(url)

        btnNextFormRequest3.setOnClickListener{
            val llFormStep3 = findViewById<LinearLayout>(R.id.llFormStep3)
            val llFormStep4 = findViewById<LinearLayout>(R.id.llFormStep4)
            // Continuar con el paso 3(TEST)
            llFormStep3.visibility = View.GONE
            llFormStep4.visibility = View.VISIBLE
        }
        // end code formstep3

        // Start code formstep4
        btnNextFormRequest4.setOnClickListener{
            val llFormStep4 = findViewById<LinearLayout>(R.id.llFormStep4)
            val llFormStep5 = findViewById<LinearLayout>(R.id.llFormStep5)
            // Continuar con el paso 4(TEST)
            llFormStep4.visibility = View.GONE
            llFormStep5.visibility = View.VISIBLE
        }
        // end code formstep4


        // Start code formstep5
        val btnSend = findViewById<Button>(R.id.btnSend)

        btnSend.setOnClickListener {
            // Mostrar un mensaje Toast
            Toast.makeText(this, "Solicitud de registro proveedor enviada", Toast.LENGTH_SHORT).show()

            // Redirigir a MenuActivity
//            val intent = Intent(this, MenuActivity::class.java)
//            startActivity(intent)

            // Finalizar la actividad actual
            finish()
        }
        // end code formstep5
    }

    override fun onBackPressed() {
        val llFormStep1 = findViewById<LinearLayout>(R.id.llFormStep1)
        val llFormStep2 = findViewById<LinearLayout>(R.id.llFormStep2)
        val llFormStep3 = findViewById<LinearLayout>(R.id.llFormStep3)
        val llFormStep4 = findViewById<LinearLayout>(R.id.llFormStep4)
        val llFormStep5 = findViewById<LinearLayout>(R.id.llFormStep5)

        when {
            llFormStep5.visibility == View.VISIBLE -> {
                llFormStep5.visibility = View.GONE
                llFormStep4.visibility = View.VISIBLE
            }
            llFormStep4.visibility == View.VISIBLE -> {
                llFormStep4.visibility = View.GONE
                llFormStep3.visibility = View.VISIBLE
            }
            llFormStep3.visibility == View.VISIBLE -> {
                llFormStep3.visibility = View.GONE
                llFormStep2.visibility = View.VISIBLE
            }
            llFormStep2.visibility == View.VISIBLE -> {
                llFormStep2.visibility = View.GONE
                llFormStep1.visibility = View.VISIBLE
            }
            llFormStep1.visibility == View.VISIBLE -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Estas seguro que quieres salir?")
                builder.setMessage("Si abandonas el registro, los datos que ingresastes se perderan")
                builder.setPositiveButton("Si, salir") { _, _ ->
                    finish()
                }

                builder.setNegativeButton("Continuar con el registro"){ dialog, _ ->
                    dialog.dismiss()
                }

                val dialog = builder.create()
                dialog.show()
            }
        }
    }
}