package iberoplast.pe.gespro.ui.modules.condition_payment

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
import iberoplast.pe.gespro.model.TypePayment
import iberoplast.pe.gespro.ui.adapters.ConditionPaymentAdapter
import iberoplast.pe.gespro.util.ActionBarUtils
import iberoplast.pe.gespro.util.PreferenceHelper
import iberoplast.pe.gespro.util.PreferenceHelper.get
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConditionPaymentActivity : AppCompatActivity() {
    private val apiService: ApiService by lazy {
        ApiService.create()
    }
    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }
    private lateinit var conditionPaymentAdapter: ConditionPaymentAdapter
    private lateinit var etSearch: EditText
    private lateinit var rvConditionsPayments: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_condition_payment)

        ActionBarUtils.setCustomTitle(
            this,
            "Condiciones de pago"
        )

        etSearch = findViewById(R.id.etSearch)
        rvConditionsPayments = findViewById(R.id.rvConditionsPayments)

        conditionPaymentAdapter = ConditionPaymentAdapter()
        rvConditionsPayments.layoutManager = LinearLayoutManager(this)
        rvConditionsPayments.adapter = conditionPaymentAdapter

        registerForContextMenu(rvConditionsPayments)
        rvConditionsPayments.isLongClickable = true

        conditionPaymentAdapter.setNewSelectedConditionPayment(null) // Inicializa el usuario seleccionado como nulo

        loadConditionsPayments()

        val btnCreateConditionPayment = findViewById<Button>(R.id.btnCreateConditionPayment)
        btnCreateConditionPayment.setOnClickListener {
            val intent = Intent(this, FormConditionPaymentActivity::class.java)
            startActivity(intent)
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No necesitas implementar esto
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString().toLowerCase()
                conditionPaymentAdapter.filterConditionsPayments(searchText)
            }

            override fun afterTextChanged(s: Editable?) {
                // No necesitas implementar esto
            }
        })
    }

    private fun loadConditionsPayments() {
        val jwt = preferences["jwt", ""]
        val call = apiService.getConditionsPayments("Bearer $jwt")
        val llLoader = findViewById<LinearLayout>(R.id.llLoader)
        val llContent = findViewById<LinearLayout>(R.id.llContent)
        llLoader.visibility = View.VISIBLE
        llContent.visibility = View.GONE
        call.enqueue(object : Callback<ArrayList<TypePayment>> {
            override fun onResponse(call: Call<ArrayList<TypePayment>>, response: Response<ArrayList<TypePayment>>) {
                llLoader.visibility = View.GONE
                llContent.visibility = View.VISIBLE
                if (response.isSuccessful) {
                    response.body()?.let {
                        conditionPaymentAdapter.updateConditionsPayments(it)
                    }
                }
            }
            override fun onFailure(call: Call<ArrayList<TypePayment>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)

        if (v?.id == R.id.rvConditionsPayments) {
            menu?.setHeaderTitle("Opciones")
            val inflater: MenuInflater = menuInflater
            inflater.inflate(R.menu.menu_options_condition_payment, menu)
        }
    }
    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit -> {
                val selectedConditionPayment = conditionPaymentAdapter.selectedConditionPayment
                val id = selectedConditionPayment?.id
                // Método a ejecutar para la opción R.id.edit
                if (id != null) {
                    editConditionPayment(id)
                }
                return true
            }
            else -> return super.onContextItemSelected(item)
        }
    }

    private fun editConditionPayment(id: Int){
        val jwt = preferences["jwt", ""]
        val call = apiService.editConditionPayment("Bearer $jwt", id)

        call.enqueue(object: Callback<TypePayment> {
            override fun onResponse(call: Call<TypePayment>, response: Response<TypePayment>) {
                if (response.isSuccessful)
                {
                    val condition_payment = response.body()
                    if (condition_payment != null){

                        val intent = Intent(this@ConditionPaymentActivity, FormConditionPaymentActivity::class.java)
                        intent.putExtra("condition_payment", condition_payment)
                        intent.putExtra("isEditing", true)
                        startActivity(intent)
                    }
                }
            }

            override fun onFailure(call: Call<TypePayment>, t: Throwable) {
            }

        })
    }
}