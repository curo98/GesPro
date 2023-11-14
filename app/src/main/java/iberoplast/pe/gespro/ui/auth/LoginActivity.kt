package iberoplast.pe.gespro.ui.auth

import android.content.Intent
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val tvGoToRegister = findViewById<TextView>(R.id.tvGoToRegister)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        // PERSISTENCIA DE DATOS CON SHARED PREFERENCES
        val preferences = PreferenceHelper.defaultPrefs(this)

        if (preferences["jwt", ""].contains(".")){
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
                        val preferences = PreferenceHelper.defaultPrefs(this@LoginActivity)
                        preferences["user_role_name"] = loginResponse.user.role?.name

                        // Continúa con el proceso de inicio de sesión
                        createSessionPreference(loginResponse.jwt)
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

    private fun createSessionPreference(jwt: String){
        val preferences = PreferenceHelper.defaultPrefs(this)
        preferences["jwt"] = jwt
    }

    private fun goToMenuActivity(isUserInput: Boolean = false) {
        val intent = Intent(this, MenuActivity::class.java)
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
