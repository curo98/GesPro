package iberoplast.pe.gespro.ui.modules

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import iberoplast.pe.gespro.R
import iberoplast.pe.gespro.ui.MenuActivity

class SuccesfulRegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_succesful_register)

        val btnReturnToMenu = findViewById<Button>(R.id.btnReturnToMenu)

        val message = intent.getStringExtra("message")

        Toast.makeText(this, message, Toast.LENGTH_LONG).show()

        btnReturnToMenu.setOnClickListener {

            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)

            // Finalizar la actividad actual
            finish()
        }
    }
}

