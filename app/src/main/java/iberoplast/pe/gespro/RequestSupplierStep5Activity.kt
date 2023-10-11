package iberoplast.pe.gespro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RequestSupplierStep5Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_supplier_step5)

        val btnSend = findViewById<Button>(R.id.btnSend)

        btnSend.setOnClickListener {
            // Mostrar un mensaje Toast
            Toast.makeText(this, "Solicitud de registro proveedor enviada", Toast.LENGTH_SHORT).show()

            // Redirigir a MenuActivity
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)

            // Finalizar la actividad actual
            finish()
        }
    }
}
