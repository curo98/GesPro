package iberoplast.pe.gespro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class RequestSupplierStep3Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_supplier_step3)

//
        val btnNextFormRequest3 = findViewById<Button>(R.id.btnNextFormRequest3)
        btnNextFormRequest3.setOnClickListener{
            val intent = Intent(this, RequestSupplierStep4Activity::class.java)
            startActivity(intent)
        }
    }
}