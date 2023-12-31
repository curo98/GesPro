package iberoplast.pe.gespro.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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

class RegisterActivity : AppCompatActivity() {

    private val apiService: ApiService by lazy {
        ApiService.create()
    }
    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this@RegisterActivity)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportActionBar?.hide()

        val userRoleName = preferences["UserRolePreferences", ""]
        Log.d("UserRoleRegisterActivity", "Rol del usuario: $userRoleName")

        if (preferences["jwt", ""].contains(".")){
            goToMenuActivity()
        }

        val tvGoToLogin = findViewById<TextView>(R.id.tvGoToLogin)

        tvGoToLogin.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)
        btnRegistrar.setOnClickListener {
            performRegister()
        }
    }

    private fun performRegister(){
        val name = findViewById<EditText>(R.id.name).text.toString().trim()
        val email = findViewById<EditText>(R.id.email).text.toString().trim()
        val password = findViewById<EditText>(R.id.password).text.toString()
        val password_confirmation = findViewById<EditText>(R.id.password_confirmation).text.toString()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || password_confirmation.isEmpty()){
            toast("Por favor complete todos los campos")
            return
        }
        if (password != password_confirmation){
            toast("Las claves de acceso no coinciden")
            return
        }
        val call = apiService.postRegister(name, email, password, password_confirmation)
        call.enqueue(object: Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful){
                    val loginResponse = response.body()
                    if(loginResponse == null){
                        toast("Se obtuvo una respuesta inesperada del servidor")
                        return
                    }
                    Log.d("LoginResponseSuccess", "Response: $loginResponse")
                    if (loginResponse.success){
                        createSessionPreference(loginResponse.jwt, loginResponse.role.name, loginResponse.user.name)
                        toast("Bienvenido ${loginResponse.role.name}")
                        goToMenuActivity(true)
                        finish()
                    }else{
                        toast("Credenciales incorrectas")
                    }
                }else{
                    toast("Intenta con otro correo")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                t.localizedMessage?.let { toast(it) }
            }

        })
    }

    private fun createSessionPreference(jwt: String, userRoleName: String, userName: String){
        preferences["jwt"] = jwt
        preferences["UserRolePreferences"] = userRoleName
        preferences["UserName"] = userName
    }
    private fun goToMenuActivity(isUserInput: Boolean = false) {

        val intent = Intent(this, MenuActivity::class.java)
        if (isUserInput){
            intent.putExtra("store_token", true)
        }
        startActivity(intent)
        finish()
    }
}