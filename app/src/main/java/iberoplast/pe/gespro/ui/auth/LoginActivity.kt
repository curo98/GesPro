package iberoplast.pe.gespro.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import iberoplast.pe.gespro.R
import iberoplast.pe.gespro.io.ApiService
import iberoplast.pe.gespro.io.response.LoginResponse
import iberoplast.pe.gespro.ui.MenuActivity
import iberoplast.pe.gespro.util.PreferenceHelper
import iberoplast.pe.gespro.util.PreferenceHelper.get
import iberoplast.pe.gespro.util.PreferenceHelper.set
import iberoplast.pe.gespro.util.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {

    private val apiService: ApiService by lazy {
        ApiService.create()
    }
    private val snackBar by lazy {
        val mainLayout = findViewById<ScrollView>(R.id.mainLayout)
        Snackbar.make(mainLayout, R.string.press_back_again, Snackbar.LENGTH_SHORT)
    }
    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this@LoginActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        val userRoleName = preferences["UserRolePreferences", ""]
        Log.d("UserRoleLoginActivity", "Rol del usuario: $userRoleName")

        val tvGoToRegister = findViewById<TextView>(R.id.tvGoToRegister)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        // PERSISTENCIA DE DATOS CON SHARED PREFERENCES

        if (preferences["jwt", ""].contains(".") && preferences["UserRolePreferences", ""].isNotBlank()) {
            // La preferencia "jwt" contiene un punto (.) y la preferencia "UserRolePreferences" no está en blanco
            goToMenuActivity()
        }

        btnLogin.setOnClickListener {
            performLogin()
        }

        tvGoToRegister.setOnClickListener{
            Toast.makeText(this,
                getString(R.string.por_favor_completa_tus_datos), Toast.LENGTH_SHORT).show()

            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun performLogin(){
        val etEmail = findViewById<EditText>(R.id.etEmail).text.toString()
        val etPassword = findViewById<EditText>(R.id.etPassword).text.toString()
        if (etEmail.trim().isEmpty() || etPassword.trim().isEmpty()){
            toast("Ingrese su correo y clave de acceso")
            return
        }

        val call = apiService.postLogin(etEmail, etPassword)
        call.enqueue(object: Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful){
                    val loginResponse = response.body()
                    if(loginResponse == null){
                        toast("Se obtuvo una respuesta inesperada del servidor")
                        return
                    }
                    if (loginResponse.success){
                        // Continúa con el proceso de inicio de sesión
                        createSessionPreference(loginResponse.jwt, loginResponse.role.name, loginResponse.user.name)
                        Log.d("LoginResponseSuccess", loginResponse.toString())
                        toast("Bienvenido ${loginResponse.user.name}")
                        goToMenuActivity(true)
                    }else{
                        toast("Credenciales incorrectas")
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                t.localizedMessage?.let { toast(it) }
            }

        })

    }

    private fun createSessionPreference(jwt: String, userRoleName: String, userName: String) {
        preferences["jwt"] = jwt
        preferences["UserRolePreferences"] = userRoleName
        preferences["UserName"] = userName
        Log.d("UserRolePreferences", userRoleName)
    }

    private fun goToMenuActivity(isUserInput: Boolean = false) {
        val intent = Intent(this@LoginActivity, MenuActivity::class.java)
        if (isUserInput){
            intent.putExtra("store_token", true)
        }
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        if (snackBar.isShown)
//            super.onBackPressed()
            onBackPressedDispatcher.onBackPressed()
        else
            snackBar.show()
    }
}
