package iberoplast.pe.gespro.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import iberoplast.pe.gespro.R
import iberoplast.pe.gespro.io.ApiService
import iberoplast.pe.gespro.ui.auth.MainActivity
import iberoplast.pe.gespro.ui.modules.requests.FormRequestSupplierActivity
import iberoplast.pe.gespro.ui.modules.requests.SupplierRequestsActivity
import iberoplast.pe.gespro.ui.modules.supplier.FormSupplierActivity
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
    private val userRoleName: String by lazy {
        preferences["user_role_name", ""]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        // Recupera el nombre del rol del usuario autenticado
        Log.d("UserRole", "Rol del usuario: $userRoleName")

        val storeToken = intent.getBooleanExtra("store_token", false)
        if (storeToken){
            storeToken()
        }
        val btnCrearSolicitud = findViewById<Button>(R.id.btnCrearSolicitud)
        val btnListProveedores = findViewById<Button>(R.id.btn_list_proveedores)
        val btnListSolicitudes = findViewById<Button>(R.id.btn_list_solicitudes)
        val btnProfile = findViewById<Button>(R.id.btnProfile)
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

        when (userRoleName) {
            "admin" -> {
                // Rol admin: Mostrar o habilitar los botones necesarios
                btnProfile.visibility = View.VISIBLE
                btnCrearSolicitud.visibility = View.GONE
                btnListProveedores.visibility = View.VISIBLE
                btnListSolicitudes.visibility = View.VISIBLE
            }
            "compras" -> {
                // Rol compras: Mostrar o habilitar los botones necesarios
                btnProfile.visibility = View.VISIBLE
                btnCrearSolicitud.visibility = View.GONE
                btnListProveedores.visibility = View.VISIBLE
                btnListSolicitudes.visibility = View.VISIBLE
            }
            "contabilidad" -> {
                // Rol contabilidad: Mostrar o habilitar los botones necesarios
                btnProfile.visibility = View.VISIBLE
                btnCrearSolicitud.visibility = View.GONE
                btnListProveedores.visibility = View.VISIBLE
                btnListSolicitudes.visibility = View.VISIBLE
            }
            "proveedor" -> {
                // Rol proveedor: Mostrar u ocultar los botones necesarios
                btnProfile.visibility = View.VISIBLE
                btnCrearSolicitud.visibility = View.GONE
                btnListProveedores.visibility = View.VISIBLE
                btnListSolicitudes.visibility = View.VISIBLE
            }
            // Agrega más casos según tus roles
            else -> {
                // Otros roles: Mostrar u ocultar los botones según sea necesario
                // Puedes establecer la visibilidad a GONE para ocultarlos completamente
                btnProfile.visibility = View.VISIBLE
                btnCrearSolicitud.visibility = View.VISIBLE
                btnListProveedores.visibility = View.GONE
                btnListSolicitudes.visibility = View.VISIBLE
            }
        }

    }

    fun editProfile(view: View){
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
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
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_in_activity, menu)

        // Accede al rol del usuario desde tus preferencias
        val preferences = PreferenceHelper.defaultPrefs(this)
        val userRoleName = preferences["user_role_name", ""]

        // Accede a los elementos de menú que deseas mostrar u ocultar según el rol del usuario
        val imAdministration = menu?.findItem(R.id.imAdministration)
        val imSuppliers = menu?.findItem(R.id.imSuppliers)
        val imRequests = menu?.findItem(R.id.imRequests)
        val imCreateRequest = menu?.findItem(R.id.imCreateRequest)
        val imListMyRequests = menu?.findItem(R.id.imListMyRequests)
        val imcreateSupplier = menu?.findItem(R.id.imCreateSupplier)

        when (userRoleName) {
            "admin" -> {
                imAdministration?.isVisible = true
                imSuppliers?.isVisible = true
                imRequests?.isVisible = true
                imCreateRequest?.isVisible = false
                imListMyRequests?.isVisible = false
            }
            "compras" -> {
                imAdministration?.isVisible = false
                imSuppliers?.isVisible = true
                imRequests?.isVisible = true
                imCreateRequest?.isVisible = false
                imListMyRequests?.isVisible = false
                imcreateSupplier?.isVisible = false
            }
            "contabilidad" -> {
                imAdministration?.isVisible = false
                imSuppliers?.isVisible = true
                imRequests?.isVisible = true
                imCreateRequest?.isVisible = false
                imListMyRequests?.isVisible = false
                imcreateSupplier?.isVisible = false
            }
            "proveedor" -> {
                imAdministration?.isVisible = false
                imSuppliers?.isVisible = false
                imRequests?.isVisible = false
                imCreateRequest?.isVisible = true
                imListMyRequests?.isVisible = true
            }
            else -> {
                imAdministration?.isVisible = false
                imSuppliers?.isVisible = false
                imRequests?.isVisible = false
                imCreateRequest?.isVisible = true
                imListMyRequests?.isVisible = true
            }
        }

        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.imMyProfile -> {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.imStates -> {
                return true
            }
            R.id.imTypePayments -> {
                return true
            }
            R.id.imMethodPayments -> {
                return true
            }
            R.id.imQuestions -> {
                return true
            }
            R.id.imRoles -> {
                return true
            }
            R.id.imUsers -> {
                return true
            }
            R.id.imCreateSupplier -> {
                val intent = Intent(this, FormSupplierActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.imListSuppliers -> {
                val intent = Intent(this, SuppliersActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.imCreateRequest -> {
                val intent = Intent(this, FormRequestSupplierActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.imListMyRequests -> {
                val intent = Intent(this, SupplierRequestsActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.imListRequests -> {
                val intent = Intent(this, SupplierRequestsActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.imLogout -> {
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
        preferences["user_role_name"] = ""
    }

    companion object {
        private const val TAG = "MenuActivity"
    }
}
