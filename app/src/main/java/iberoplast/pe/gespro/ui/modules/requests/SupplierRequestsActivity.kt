package iberoplast.pe.gespro.ui.modules.requests

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.ContextMenu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import iberoplast.pe.gespro.R
import iberoplast.pe.gespro.io.ApiService
import iberoplast.pe.gespro.model.SupplierRequest
import iberoplast.pe.gespro.ui.adapters.SupplierRequestAdapter
import iberoplast.pe.gespro.util.PreferenceHelper
import iberoplast.pe.gespro.util.PreferenceHelper.get
import iberoplast.pe.gespro.util.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SupplierRequestsActivity : AppCompatActivity() {
    private val apiService: ApiService by lazy {
        ApiService.create()
    }
    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }
    private lateinit var etSearch: EditText
    private lateinit var supplierRequestAdapter: SupplierRequestAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supplier_requests)

        etSearch = findViewById(R.id.etSearch)

        supplierRequestAdapter = SupplierRequestAdapter()
        val rvSupplierRequests = findViewById<RecyclerView>(R.id.rvSupplierRequests)
        rvSupplierRequests.layoutManager = LinearLayoutManager(this)
        rvSupplierRequests.adapter = supplierRequestAdapter
        registerForContextMenu(rvSupplierRequests)
        rvSupplierRequests.isLongClickable = true
        supplierRequestAdapter.setNewSelectedRequest(null) // Inicializa el usuario seleccionado como nulo
        loadSupplierRequests()

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No necesitas implementar esto
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString().toLowerCase()
                supplierRequestAdapter.filterRequests(searchText)
            }

            override fun afterTextChanged(s: Editable?) {
                // No necesitas implementar esto
            }
        })
    }
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)

        if (v?.id == R.id.rvSupplierRequests) {
            val inflater: MenuInflater = menuInflater
            inflater.inflate(R.menu.menu_options, menu)
        }
    }
    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show -> {
                val selectedRequest = supplierRequestAdapter.selectedRequest
                val id = selectedRequest?.id
                // Método a ejecutar para la opción R.id.opc2
                if (id != null) {
                    showRequest(id)
                }
                return true
            }
            else -> return super.onContextItemSelected(item)
        }
    }
    private fun showRequest(id: Int) {
        val jwt = preferences["jwt", ""]
        val call = apiService.getRequestDetails("Bearer $jwt", id)

        call.enqueue(object : Callback<SupplierRequest> {
            override fun onResponse(call: Call<SupplierRequest>, response: Response<SupplierRequest>) {
                if (response.isSuccessful) {
                    val request = response.body()

                    if (request != null) {

                        // Mostrar el mensaje en el registro (logcat)
                        Log.d("RequestDetails", request.toString())

                        val intent = Intent(this@SupplierRequestsActivity, ShowRequestActivity::class.java)
                        intent.putExtra("request_details", request)
                        startActivity(intent)
                    }
                } else {
                    // Manejar la respuesta no exitosa, por ejemplo, mostrar un mensaje de error
                    toast("Error al obtener los detalles del proveedor")
                }
            }

            override fun onFailure(call: Call<SupplierRequest>, t: Throwable) {
                // Manejar el error en la solicitud, por ejemplo, mostrar un mensaje de error
                toast("Error en la solicitud: ${t.message}")
            }
        })
    }
    private fun loadSupplierRequests() {
        val jwt = preferences["jwt", ""]
        val call = apiService.getSupplierRequests("Bearer $jwt")
        val pbRequests = findViewById<ProgressBar>(R.id.pbRequests)
        val rvSupplierRequests = findViewById<RecyclerView>(R.id.rvSupplierRequests)

        pbRequests.visibility = View.VISIBLE
        rvSupplierRequests.visibility = View.GONE
        etSearch.visibility = View.GONE
        call.enqueue(object : Callback<ArrayList<SupplierRequest>> {
            override fun onResponse(call: Call<ArrayList<SupplierRequest>>, response: Response<ArrayList<SupplierRequest>>) {
                if (response.isSuccessful) {
                    pbRequests.visibility = View.GONE
                    rvSupplierRequests.visibility = View.VISIBLE
                    etSearch.visibility = View.VISIBLE
                    response.body()?.let {
                        supplierRequestAdapter.updateRequests(it)
                    }
                }
            }
            override fun onFailure(call: Call<ArrayList<SupplierRequest>>, t: Throwable) {
                showToast("Error en la solicitud: ${t.message}")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this@SupplierRequestsActivity, message, Toast.LENGTH_SHORT).show()
    }
}

