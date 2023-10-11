package iberoplast.pe.gespro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import iberoplast.pe.gespro.PreferenceHelper.set

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val btnCrearSolicitud = findViewById<Button>(R.id.btnCrearSolicitud)
        val btnListProveedores = findViewById<Button>(R.id.btn_list_proveedores)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        btnCrearSolicitud.setOnClickListener{
            val intent = Intent(this, RequestSupplierStep1Activity::class.java)
            startActivity(intent)
        }

        btnListProveedores.setOnClickListener{
            val intent = Intent(this, SuppliersActivity::class.java)
            startActivity(intent)
        }

        btnLogout.setOnClickListener{
            clearSessionPreference()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun clearSessionPreference() {
//        val preferences = getSharedPreferences("general", MODE_PRIVATE)
//        val editor = preferences.edit()
//        editor.putBoolean("session", false)
//        editor.apply()

        val preferences = PreferenceHelper.defaultPrefs(this)
        preferences["session"] = false
    }
}