package iberoplast.pe.gespro.ui.modules.requests

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import iberoplast.pe.gespro.R
import iberoplast.pe.gespro.io.ApiService
import iberoplast.pe.gespro.model.SupplierRequest
import iberoplast.pe.gespro.ui.adapters.SupplierRequestAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SupplierRequestsActivity : AppCompatActivity() {
    private val apiService: ApiService by lazy {
        ApiService.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supplier_requests)

        val recyclerView = findViewById<RecyclerView>(R.id.rvSupplierRequests)
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadSupplierRequests()
    }

    private fun loadSupplierRequests() {
        val call = apiService.getSupplierRequests()

        call.enqueue(object : Callback<ArrayList<SupplierRequest>> {
            override fun onResponse(call: Call<ArrayList<SupplierRequest>>, response: Response<ArrayList<SupplierRequest>>) {
                if (response.isSuccessful) {
                    val supplierRequests = response.body()

                    if (supplierRequests != null) {
                        // Los datos se obtuvieron correctamente, actualiza el adaptador de tu RecyclerView
                        val rvSupplierRequests = findViewById<RecyclerView>(R.id.rvSupplierRequests)
                        rvSupplierRequests.layoutManager = LinearLayoutManager(this@SupplierRequestsActivity)
                        rvSupplierRequests.adapter = SupplierRequestAdapter(supplierRequests)
                    } else {
                        // Maneja el caso donde la respuesta no contiene datos válidos
                        showToast("La respuesta no contiene datos válidos")
                    }
                } else {
                    // Maneja la respuesta de error aquí
                    showToast("Error en la respuesta de la API: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ArrayList<SupplierRequest>>, t: Throwable) {
                // Maneja errores de red u otras excepciones
                showToast("Error en la solicitud: ${t.message}")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this@SupplierRequestsActivity, message, Toast.LENGTH_SHORT).show()
    }
}

