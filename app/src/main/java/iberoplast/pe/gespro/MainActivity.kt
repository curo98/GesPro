package iberoplast.pe.gespro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import iberoplast.pe.gespro.PreferenceHelper.get
import iberoplast.pe.gespro.PreferenceHelper.set


class MainActivity : AppCompatActivity() {



    private val snackBar by lazy {
        val mainLayout = findViewById<ScrollView>(R.id.mainLayout)
        Snackbar.make(mainLayout, R.string.press_back_again, Snackbar.LENGTH_SHORT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvGoToRegister = findViewById<TextView>(R.id.tvGoToRegister)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        /*
        // PERSISTENCIA DE DATOS CON SHARED PREFERENCES
        val preferences = getSharedPreferences("general", Context.MODE_PRIVATE)
        val session = preferences.getBoolean("session", false)*/
        val preferences = PreferenceHelper.defaultPrefs(this)

        if (preferences["session", false]){
            goToMenuActivity()
        }

        tvGoToRegister.setOnClickListener{
            Toast.makeText(this,
                getString(R.string.por_favor_completa_tus_datos), Toast.LENGTH_SHORT).show()

            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
//          Este intent hace que nos lleve a otro activity

//            finish()
        }

        btnLogin.setOnClickListener {
            createSessionPreference()
            goToMenuActivity()
        }
    }

    private fun createSessionPreference(){
//        val preferences = getSharedPreferences("general", Context.MODE_PRIVATE)
//        val editor = preferences.edit()
//        editor.putBoolean("session", true)
//        editor.apply()

        // Esto llama el metodo set del Helper, este metodo va a recibir la key que le estamos
        // enviando y el valor que seria TRUE.
        val preferences = PreferenceHelper.defaultPrefs(this)
        preferences["session"] = true
    }

    private fun goToMenuActivity() {
        val intent = Intent(this, MenuActivity::class.java)
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