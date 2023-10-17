package iberoplast.pe.gespro.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import iberoplast.pe.gespro.R

class RequestSupplierStep1Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_supplier_step1)


        val btnNext = findViewById<Button>(R.id.btnNextFormRequest1)

        btnNext.setOnClickListener{
            val etNicRuc = findViewById<EditText>(R.id.etNicRuc)
            val nicRucText = etNicRuc.text.toString().trim()

            if (nicRucText.isEmpty()) {
                etNicRuc.error = "El NIC o RUC no puede estar vacío"
            } else if (!nicRucText.matches(Regex("^[0-9]+$"))) {
                etNicRuc.error = "Solo se permiten números en el NIC o RUC"
            } else if (nicRucText.length < 10) {
                etNicRuc.error = "El NIC o RUC es demasiado corto"
            } else {
                // Continuar con el paso 2
                val intent = Intent(this, RequestSupplierStep2Activity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onBackPressed() {
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