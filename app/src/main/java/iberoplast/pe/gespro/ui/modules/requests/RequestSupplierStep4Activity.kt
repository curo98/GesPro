package iberoplast.pe.gespro.ui.modules.requests

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import iberoplast.pe.gespro.R

class RequestSupplierStep4Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_supplier_step4)

        val btnNextFormRequest4 = findViewById<Button>(R.id.btnNextFormRequest4)

        btnNextFormRequest4.setOnClickListener{
            val intent = Intent(this, RequestSupplierStep5Activity::class.java)
            startActivity(intent)
        }
    }
}