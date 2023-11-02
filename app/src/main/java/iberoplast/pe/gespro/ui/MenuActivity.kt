package iberoplast.pe.gespro.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import iberoplast.pe.gespro.R
import iberoplast.pe.gespro.io.ApiService
import iberoplast.pe.gespro.ui.auth.MainActivity
import iberoplast.pe.gespro.ui.modules.requests.FormRequestSupplierActivity
import iberoplast.pe.gespro.ui.modules.requests.SupplierRequestsActivity
import iberoplast.pe.gespro.ui.modules.state.StateRequestActivity
import iberoplast.pe.gespro.ui.modules.supplier.SuppliersActivity
import iberoplast.pe.gespro.util.PreferenceHelper
import iberoplast.pe.gespro.util.PreferenceHelper.get
import iberoplast.pe.gespro.util.PreferenceHelper.set
import iberoplast.pe.gespro.util.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuActivity : AppCompatActivity() {
    private val apiService by lazy {
        ApiService.create()
    }

    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val storeToken = intent.getBooleanExtra("store_token", false)
        if (storeToken){
            storeToken()
        }

        val btnCrearSolicitud = findViewById<Button>(R.id.btnCrearSolicitud)
        val btnListProveedores = findViewById<Button>(R.id.btn_list_proveedores)
        val btnListSolicitudes = findViewById<Button>(R.id.btn_list_solicitudes)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        btnCrearSolicitud.setOnClickListener{
            val intent = Intent(this, FormRequestSupplierActivity::class.java)
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
            performLogout()
        }
    }

    private fun storeToken()
    {
        val jwt = preferences["jwt", ""]
        val authHeader = "Bearer $jwt"
        Firebase.messaging.token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                println("el token no fue generado")
                return@addOnCompleteListener
            }
            // Get new FCM registration token
            val deviceToken = task.result

            val call = apiService.postToken(authHeader, deviceToken)
            call.enqueue(object: Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful){
                        Log.d(TAG, "Token registrado correctamente")
                    }else{
                        Log.d(TAG, "Ocurrio un error al registrar el token")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    t.localizedMessage?.let { toast(it) }
                }

            })
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
                val intent = Intent(this, FormRequestSupplierActivity::class.java)
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
                performLogout()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun goToLogin(){
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun performLogout(){
        val jwt = preferences["jwt", ""]
        val call = apiService.postLogout("Bearer $jwt")
        call.enqueue(object: Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                clearSessionPreference()
                goToLogin()
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                t.localizedMessage?.let { toast(it) }
            }

        })
    }
    /* END MENU CODE */

    private fun clearSessionPreference() {
        preferences["jwt"] = ""
    }

    companion object {
        private const val TAG = "MenuActivity"
    }
}