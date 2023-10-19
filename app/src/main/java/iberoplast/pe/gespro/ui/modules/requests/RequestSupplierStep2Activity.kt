package iberoplast.pe.gespro.ui.modules.requests

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import iberoplast.pe.gespro.R

class RequestSupplierStep2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_supplier_step2)

        val btnNextFormRequest2 = findViewById<Button>(R.id.btnNextFormRequest2)
        btnNextFormRequest2.setOnClickListener{
            val intent = Intent(this, RequestSupplierStep3Activity::class.java)
            startActivity(intent)
        }

        val spPaises = findViewById<Spinner>(R.id.spinnerPaises)
        val spDepartamentos = findViewById<Spinner>(R.id.spinnerDepartamentos)
        val spDistritos = findViewById<Spinner>(R.id.spinnerDistritos)

        val paises = arrayOf("Peru", "Colombia", "Chile", "Venezuela")
        val departamentos = arrayOf("Lima", "Iquitos", "Ica", "Puno")
        val distritos = arrayOf("San vicente de canete", "La victoria", "Pucusana", "Juliaca")

        spPaises.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, paises)

        spDepartamentos.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, departamentos)

        spDistritos.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, distritos)

//        val spinnerPaises = findViewById<Spinner>(R.id.spinnerPaises)
//
//        val paises = arrayOf("Peru", "Colombia", "Chile", "Venezuela")
//
//        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, paises)
//        spinnerPaises.adapter = adapter
    }
}