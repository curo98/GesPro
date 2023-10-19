package iberoplast.pe.gespro.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import iberoplast.pe.gespro.ui.helpers.PreferenceHelper
import iberoplast.pe.gespro.ui.helpers.PreferenceHelper.set
import iberoplast.pe.gespro.R
import iberoplast.pe.gespro.ui.auth.MainActivity
import iberoplast.pe.gespro.ui.modules.requests.RegisterRequestSupplierActivity
import iberoplast.pe.gespro.ui.modules.requests.SupplierRequestsActivity
import iberoplast.pe.gespro.ui.modules.state.StateRequestActivity
import iberoplast.pe.gespro.ui.modules.supplier.SuppliersActivity

class MenuActivity : AppCompatActivity() {

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.proveedores -> {
                // Hacer algo cuando se selecciona "Proveedores"

                return true
            }
            R.id.solic -> {
                // Hacer algo cuando se selecciona "Solicitudes"

                return true
            }
            R.id.submenu_item_1 -> {
                // Hacer algo cuando se selecciona "Submenú 1"
                val intent = Intent(this, SuppliersActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.submenu_item_2 -> {
                // Hacer algo cuando se selecciona "Submenú 2"
                return true
            }

            R.id.submenu_item_3 -> {
                // Hacer algo cuando se selecciona "Submenú 1"
                val intent = Intent(this, SupplierRequestsActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.submenu_item_4 -> {
                // Hacer algo cuando se selecciona "Submenú 2"
                return true
            }

            R.id.btnCrearSolicitud -> {
                // Hacer algo cuando se selecciona "Proveedores"
                val intent = Intent(this, RegisterRequestSupplierActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.btnVerEstadoSol -> {
                // Hacer algo cuando se selecciona "Solicitudes"
                val intent = Intent(this, StateRequestActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.logout -> {
                clearSessionPreference()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
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