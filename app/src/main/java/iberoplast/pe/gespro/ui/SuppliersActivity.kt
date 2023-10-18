package iberoplast.pe.gespro.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import iberoplast.pe.gespro.R
import iberoplast.pe.gespro.io.ApiService
import iberoplast.pe.gespro.model.Supplier
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SuppliersActivity : AppCompatActivity() {

    private val apiService: ApiService by lazy {
        ApiService.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suppliers)

//        val suppliers = ArrayList<Supplier>()
//        suppliers.add(Supplier(1, "Juanito", "Juanito@gmail.com", "Rechazado"))
//        suppliers.add(Supplier(2, "Alfonso", "Alfonso@gmail.com", "Aprobado"))
//        suppliers.add(Supplier(3, "Daniel", "Daniel@gmail.com", "Por validar"))

        val rvSuppliers = findViewById<RecyclerView>(R.id.rvProveedores)
        rvSuppliers.layoutManager = LinearLayoutManager(this)
//        rvSuppliers.adapter = SupplierAdapter(suppliers)

        loadSuppliers()

        // Registra el RecyclerView para el menú contextual
//        registerForContextMenu(rvSuppliers)

    }

//    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
//        super.onCreateContextMenu(menu, v, menuInfo)
//
//        if (v?.id == R.id.rvProveedores) {
//            val inflater: MenuInflater = menuInflater
//            inflater.inflate(R.menu.menu_options, menu)
//        }
//    }

//    override fun onContextItemSelected(item: MenuItem): Boolean {
//        // Maneja las opciones del menú contextual aquí
//        when (item.itemId) {
//            R.id.opc2 -> {
//                // Lógica para la opción 1
//                return true
//            }
//            R.id.opc1 -> {
//                // Lógica para la opción 2
//                return true
//            }
//            else -> return super.onContextItemSelected(item)
//        }
//    }

    private fun loadSuppliers() {
        val call = apiService.getSuppliers()

        call.enqueue(object : Callback<ArrayList<Supplier>> {
            override fun onResponse(call: Call<ArrayList<Supplier>>, response: Response<ArrayList<Supplier>>) {
                if (response.isSuccessful) {
                    val suppliers = response.body()

                    if (suppliers != null) {
                        // Los datos se obtuvieron correctamente, actualiza el adaptador de tu RecyclerView
                        val rvProveedores = findViewById<RecyclerView>(R.id.rvProveedores)
                        rvProveedores.layoutManager = LinearLayoutManager(this@SuppliersActivity)
                        rvProveedores.adapter = SupplierAdapter(suppliers)
                    } else {
                        // Maneja el caso donde la respuesta no contiene datos válidos
                        showToast("La respuesta no contiene datos válidos")
                    }
                } else {
                    // Maneja la respuesta de error aquí
                    showToast("Error en la respuesta de la API: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ArrayList<Supplier>>, t: Throwable) {
                // Maneja errores de red u otras excepciones
                showToast("Error en la solicitud: ${t.message}")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this@SuppliersActivity, message, Toast.LENGTH_SHORT).show()
    }
}