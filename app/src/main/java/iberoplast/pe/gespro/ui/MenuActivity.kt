package iberoplast.pe.gespro.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import iberoplast.pe.gespro.PreferenceHelper
import iberoplast.pe.gespro.PreferenceHelper.set
import iberoplast.pe.gespro.R

class MenuActivity : AppCompatActivity() {

    var tvPrueba: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val btnCrearSolicitud = findViewById<Button>(R.id.btnCrearSolicitud)
        val btnListProveedores = findViewById<Button>(R.id.btn_list_proveedores)
        val btnListSolicitudes = findViewById<Button>(R.id.btn_list_solicitudes)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        btnCrearSolicitud.setOnClickListener{
            val intent = Intent(this, RegisterRequestSupplierActivity::class.java)
            startActivity(intent)
        }

        btnListProveedores.setOnClickListener{
            val intent = Intent(this, SuppliersActivity::class.java)
            startActivity(intent)
        }

        btnListSolicitudes.setOnClickListener{
            val intent = Intent(this, SupplierRequestsActivity::class.java)
            startActivity(intent)
        }

        btnLogout.setOnClickListener{
            clearSessionPreference()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    /* START MENU */
    override fun onCreateOptionsMenu(mimenu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_in_activity, mimenu)
        return true
    }

    override fun onOptionsItemSelected(opcion_menu: MenuItem): Boolean {
        val id = opcion_menu.itemId
        if (id == R.id.proveedores) {
//            Toast.makeText(this, "Configuracion", Toast.LENGTH_SHORT).show()
//            tvPrueba!!.text = "Menu Configuracion"
            val intent = Intent(this, SuppliersActivity::class.java)
            startActivity(intent)
            return true
        }
        if (id == R.id.solic){
            val intent = Intent(this, RegisterRequestSupplierActivity::class.java)
            startActivity(intent)
        }
        if (id == R.id.logout) {
            clearSessionPreference()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
            return true
        }
        return super.onOptionsItemSelected(opcion_menu)
    }
    /* END MENU CODE */

    private fun clearSessionPreference() {
//        val preferences = getSharedPreferences("general", MODE_PRIVATE)
//        val editor = preferences.edit()
//        editor.putBoolean("session", false)
//        editor.apply()

        val preferences = PreferenceHelper.defaultPrefs(this)
        preferences["session"] = false
    }
}