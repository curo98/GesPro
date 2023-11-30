package iberoplast.pe.gespro.ui.modules.method_payment

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
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import iberoplast.pe.gespro.R
import iberoplast.pe.gespro.io.ApiService
import iberoplast.pe.gespro.model.MethodPayment
import iberoplast.pe.gespro.ui.adapters.MethodPaymentAdapter
import iberoplast.pe.gespro.util.ActionBarUtils
import iberoplast.pe.gespro.util.PreferenceHelper
import iberoplast.pe.gespro.util.PreferenceHelper.get
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MethodPaymentActivity : AppCompatActivity() {
    private val apiService: ApiService by lazy {
        ApiService.create()
    }
    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }

    private lateinit var methodPaymentAdapter: MethodPaymentAdapter
    private lateinit var etSearch: EditText
    private lateinit var rvMethodsPayments: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_method_payment)

        ActionBarUtils.setCustomTitle(
            this,
            "Metodos de pago"
        )

        etSearch = findViewById(R.id.etSearch)
        rvMethodsPayments = findViewById(R.id.rvMethodsPayments)

        methodPaymentAdapter = MethodPaymentAdapter()
        rvMethodsPayments.layoutManager = LinearLayoutManager(this)
        rvMethodsPayments.adapter = methodPaymentAdapter

        registerForContextMenu(rvMethodsPayments)
        rvMethodsPayments.isLongClickable = true

        methodPaymentAdapter.setNewSelectedMethodPayment(null) // Inicializa el usuario seleccionado como nulo

        loadMethodsPayments()

        val btnCreateMethodPayment = findViewById<Button>(R.id.btnCreateMethodPayment)
        btnCreateMethodPayment.setOnClickListener {
            val intent = Intent(this, FormMethodPaymentActivity::class.java)
            startActivity(intent)
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No necesitas implementar esto
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString().toLowerCase()
                methodPaymentAdapter.filterMethodsPayments(searchText)
            }

            override fun afterTextChanged(s: Editable?) {
                // No necesitas implementar esto
            }
        })
    }

    private fun loadMethodsPayments() {
        val jwt = preferences["jwt", ""]
        val call = apiService.getMethodsPayments("Bearer $jwt")
        val llLoader = findViewById<LinearLayout>(R.id.llLoader)
        val llContent = findViewById<LinearLayout>(R.id.llContent)
        llLoader.visibility = View.VISIBLE
        llContent.visibility = View.GONE
        call.enqueue(object : Callback<ArrayList<MethodPayment>> {
            override fun onResponse(call: Call<ArrayList<MethodPayment>>, response: Response<ArrayList<MethodPayment>>) {
                llLoader.visibility = View.GONE
                llContent.visibility = View.VISIBLE
                if (response.isSuccessful) {
                    response.body()?.let {
                        methodPaymentAdapter.updateMethodsPayments(it)
                    }
                }
            }
            override fun onFailure(call: Call<ArrayList<MethodPayment>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)

        if (v?.id == R.id.rvMethodsPayments) {
            menu?.setHeaderTitle("Opciones")
            val inflater: MenuInflater = menuInflater
            inflater.inflate(R.menu.menu_options_method_payment, menu)
        }
    }
    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit -> {
                val selectedMethodPayment = methodPaymentAdapter.selectedMethodPayment
                val id = selectedMethodPayment?.id
                // Método a ejecutar para la opción R.id.edit
                if (id != null) {
                    editMethodPayment(id)
                }
                return true
            }
            else -> return super.onContextItemSelected(item)
        }
    }

    private fun editMethodPayment(id: Int){
        val jwt = preferences["jwt", ""]
        val call = apiService.editMethodPayment("Bearer $jwt", id)

        call.enqueue(object: Callback<MethodPayment> {
            override fun onResponse(call: Call<MethodPayment>, response: Response<MethodPayment>) {
                if (response.isSuccessful)
                {
                    val method_payment = response.body()
                    if (method_payment != null){

                        val intent = Intent(this@MethodPaymentActivity, FormMethodPaymentActivity::class.java)
                        intent.putExtra("method_payment", method_payment)
                        intent.putExtra("isEditing", true)
                        startActivity(intent)
                    }
                }
            }

            override fun onFailure(call: Call<MethodPayment>, t: Throwable) {
            }

        })
    }
}