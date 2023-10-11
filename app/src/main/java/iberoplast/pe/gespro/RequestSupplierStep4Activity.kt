package iberoplast.pe.gespro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class RequestSupplierStep4Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_supplier_step4)

        val btnFormRequest4 = findViewById<Button>(R.id.btnFormRequest4)

        btnFormRequest4.setOnClickListener{
            val intent = Intent(this, RequestSupplierStep5Activity::class.java)
            startActivity(intent)
        }
    }
}