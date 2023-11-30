package iberoplast.pe.gespro.ui.modules.question

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.ContextMenu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import iberoplast.pe.gespro.R
import iberoplast.pe.gespro.io.ApiService
import iberoplast.pe.gespro.model.Question
import iberoplast.pe.gespro.ui.adapters.QuestionAdapter
import iberoplast.pe.gespro.util.ActionBarUtils
import iberoplast.pe.gespro.util.PreferenceHelper
import iberoplast.pe.gespro.util.PreferenceHelper.get
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuestionActivity : AppCompatActivity() {
    private val apiService: ApiService by lazy {
        ApiService.create()
    }
    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }
    private lateinit var questionAdapter: QuestionAdapter
    private lateinit var etSearch: EditText
    private lateinit var rvQuestions: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        ActionBarUtils.setCustomTitle(
            this,
            "Preguntas"
        )

        etSearch = findViewById(R.id.etSearch)
        rvQuestions = findViewById(R.id.rvQuestions)

        questionAdapter = QuestionAdapter()
        rvQuestions.layoutManager = LinearLayoutManager(this)
        rvQuestions.adapter = questionAdapter

        registerForContextMenu(rvQuestions)
        rvQuestions.isLongClickable = true

        questionAdapter.setNewSelectedQuestion(null) // Inicializa el usuario seleccionado como nulo

        loadQuestions()

        val btnCreateQuestion = findViewById<Button>(R.id.btnCreateQuestion)
        btnCreateQuestion.setOnClickListener {
            val intent = Intent(this, FormQuestionActivity::class.java)
            startActivity(intent)
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No necesitas implementar esto
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString().toLowerCase()
                questionAdapter.filterQuestions(searchText)
            }

            override fun afterTextChanged(s: Editable?) {
                // No necesitas implementar esto
            }
        })
    }

    private fun loadQuestions() {
        val jwt = preferences["jwt", ""]
        val call = apiService.getQuestions("Bearer $jwt")
        val llLoader = findViewById<LinearLayout>(R.id.llLoader)
        val llContent = findViewById<LinearLayout>(R.id.llContent)
        llLoader.visibility = View.VISIBLE
        llContent.visibility = View.GONE
        call.enqueue(object : Callback<ArrayList<Question>> {
            override fun onResponse(call: Call<ArrayList<Question>>, response: Response<ArrayList<Question>>) {
                llLoader.visibility = View.GONE
                llContent.visibility = View.VISIBLE
                if (response.isSuccessful) {
                    response.body()?.let {
                        questionAdapter.updateQuestions(it)
                    }
                }
            }
            override fun onFailure(call: Call<ArrayList<Question>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)

        if (v?.id == R.id.rvQuestions) {
            menu?.setHeaderTitle("Opciones")
            val inflater: MenuInflater = menuInflater
            inflater.inflate(R.menu.menu_options_question, menu)
        }
    }
    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit -> {
                val selectedQuestion = questionAdapter.selectedQuestion
                val id = selectedQuestion?.id
                // Método a ejecutar para la opción R.id.edit
                if (id != null) {
                    editQuestion(id)
                }
                return true
            }
            else -> return super.onContextItemSelected(item)
        }
    }

    private fun editQuestion(id: Int){
        val jwt = preferences["jwt", ""]
        val call = apiService.editQuestion("Bearer $jwt", id)

        call.enqueue(object: Callback<Question> {
            override fun onResponse(call: Call<Question>, response: Response<Question>) {
                if (response.isSuccessful)
                {
                    val question = response.body()
                    if (question != null){

                        val intent = Intent(this@QuestionActivity, FormQuestionActivity::class.java)
                        intent.putExtra("question", question)
                        intent.putExtra("isEditing", true)
                        startActivity(intent)
                    }
                }
            }

            override fun onFailure(call: Call<Question>, t: Throwable) {
            }

        })
    }
}