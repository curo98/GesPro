package iberoplast.pe.gespro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class RequestSupplierStep1Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_supplier_step1)
        val btnFormRequest1 = findViewById<Button>(R.id.btnFormRequest1)

        btnFormRequest1.setOnClickListener{
            val intent = Intent(this, RequestSupplierStep2Activity::class.java)
            startActivity(intent)
        }
    }
}