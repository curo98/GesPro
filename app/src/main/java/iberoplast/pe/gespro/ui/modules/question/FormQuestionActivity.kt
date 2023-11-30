package iberoplast.pe.gespro.ui.modules.question

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import iberoplast.pe.gespro.R
import iberoplast.pe.gespro.io.ApiService
import iberoplast.pe.gespro.model.Question
import iberoplast.pe.gespro.util.ActionBarUtils
import iberoplast.pe.gespro.util.PreferenceHelper
import iberoplast.pe.gespro.util.PreferenceHelper.get
import iberoplast.pe.gespro.util.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormQuestionActivity : AppCompatActivity() {
    private val apiService by lazy {
        ApiService.create()
    }
    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }
    private val etQuestion by lazy { findViewById<EditText>(R.id.etQuestion) }
    private val btnFormQuestion by lazy { findViewById<Button>(R.id.btnFormQuestion) }
    private val btnGoToList by lazy { findViewById<Button>(R.id.btnGoToList) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_question)

        val isEditing = intent.getBooleanExtra("isEditing", false)
        val question = intent.getParcelableExtra<Question>("question")

        // Si isEditing es true, estás editando un proveedor, muestra los datos existentes
        if (isEditing && question != null) {
            ActionBarUtils.setCustomTitle(
                this,
                "Editando pregunta con Id: ${question.id}"
            )
            etQuestion.setText(question.question)
            btnFormQuestion.text = "Actualizar"
            btnFormQuestion.setOnClickListener {
                val id = question.id
                executeMethodUpdate(id)
            }
        } else {
            ActionBarUtils.setCustomTitle(
                this,
                "Registro de nueva pregunta"
            )
            btnFormQuestion.text = "Crear"
            btnFormQuestion.setOnClickListener {
                executeMethodCreate()
            }
        }
        btnGoToList.setOnClickListener {
            val intent = Intent(this, QuestionActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun executeMethodCreate() {
        val jwt = preferences["jwt", ""]
        val question = findViewById<EditText>(R.id.etQuestion).text.toString()

        val call = apiService.postQuestion("Bearer $jwt", question)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(
                call: Call<Void>,
                response: Response<Void>
            ) {
                if (response.isSuccessful) {
                    val intent = Intent(this@FormQuestionActivity, QuestionActivity::class.java)
                    toast("Pregunta creada correctamente")
                    startActivity(intent)
                    finish()
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Manejar errores de la solicitud
            }
        })
    }

    private fun executeMethodUpdate(id: Int) {
        val jwt = preferences["jwt", ""]
        val question = findViewById<EditText>(R.id.etQuestion).text.toString()

        val call = apiService.updateQuestion("Bearer $jwt", id, question)
        call.enqueue(object: Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    toast("Pregunta actualizada correctamente")
                    val intent = Intent(this@FormQuestionActivity, QuestionActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
            }
        })
    }
}