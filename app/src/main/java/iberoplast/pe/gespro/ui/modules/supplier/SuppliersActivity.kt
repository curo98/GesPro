package iberoplast.pe.gespro.ui.modules.supplier

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.ContextMenu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import iberoplast.pe.gespro.R
import iberoplast.pe.gespro.io.ApiService
import iberoplast.pe.gespro.model.Supplier
import iberoplast.pe.gespro.ui.adapters.SupplierAdapter
import iberoplast.pe.gespro.util.PreferenceHelper
import iberoplast.pe.gespro.util.PreferenceHelper.get
import iberoplast.pe.gespro.util.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SuppliersActivity : AppCompatActivity() {

    private val apiService: ApiService by lazy {
        ApiService.create()
    }
    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }
    private val userRoleName: String by lazy {
        preferences["user_role_name", ""]
    }
    private lateinit var etSearch: EditText
    private lateinit var btnCreate: Button
    private lateinit var supplierAdapter: SupplierAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suppliers)

        etSearch = findViewById(R.id.etSearch)

        supplierAdapter = SupplierAdapter()
        val rvSuppliers = findViewById<RecyclerView>(R.id.rvProveedores)
        rvSuppliers.layoutManager = LinearLayoutManager(this)
        rvSuppliers.adapter = supplierAdapter
        registerForContextMenu(rvSuppliers)
        rvSuppliers.isLongClickable = true
        supplierAdapter.setNewSelectedSupplier(null)
        loadSuppliers()

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No necesitas implementar esto
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString().toLowerCase()
                supplierAdapter.filterSuppliers(searchText)
            }

            override fun afterTextChanged(s: Editable?) {
                // No necesitas implementar esto
            }
        })
        btnCreate = findViewById(R.id.btnCreateSupplier)
        if (userRoleName == "admin") {
            btnCreate.visibility = View.VISIBLE
        }

        btnCreate.setOnClickListener {
            val intent = Intent(this, FormSupplierActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)

        if (v?.id == R.id.rvProveedores) {
            val inflater: MenuInflater = menuInflater
            inflater.inflate(R.menu.menu_options, menu)
        }
    }
    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show -> {
                val selectedSupplier = supplierAdapter.selectedSupplier
                val id = selectedSupplier?.id
                // Método a ejecutar para la opción R.id.opc2
                if (id != null) {
                    showSupplier(id)
                }
                return true
            }
            R.id.edit -> {
                val selectedSupplier = supplierAdapter.selectedSupplier
                val id = selectedSupplier?.id
                // Método a ejecutar para la opción R.id.opc2
                if (id != null) {
                    editSupplier(id)
                }
                return true
            }
            else -> return super.onContextItemSelected(item)
        }
    }
    private fun showSupplier(id: Int) {
        val jwt = preferences["jwt", ""]
        val call = apiService.getSupplierDetails("Bearer $jwt", id)

        call.enqueue(object : Callback<Supplier> {
            override fun onResponse(call: Call<Supplier>, response: Response<Supplier>) {
                if (response.isSuccessful) {
                    val supplier = response.body()

                    if (supplier != null) {
                        val message = "ID: ${supplier.id}\n" +
                                "Nacionalidad: ${supplier.nacionality}\n" +
                                "NIC/RUC: ${supplier.nic_ruc}\n" +
                                "Estado: ${supplier.state}\n" +
                                "Creado en: ${supplier.created_at}\n" +
                                "Actualizado en: ${supplier.updated_at}"

                        // Mostrar el mensaje en el registro (logcat)
                        Log.d("SupplierDetails", message)
//                        val intent = Intent(this@SuppliersActivity, ShowSupplierActivity::class.java)
//                        intent.putExtra("supplier_details", supplier)
//                        startActivity(intent)
                        val intent = Intent(this@SuppliersActivity, ShowSupplierActivity::class.java)
                        intent.putExtra("supplier_details", supplier)
                        startActivity(intent)
                    }
                } else {
                    // Manejar la respuesta no exitosa, por ejemplo, mostrar un mensaje de error
                    toast("Error al obtener los detalles del proveedor")
                }
            }

            override fun onFailure(call: Call<Supplier>, t: Throwable) {
                // Manejar el error en la solicitud, por ejemplo, mostrar un mensaje de error
                toast("Error en la solicitud: ${t.message}")
            }
        })
    }

    private fun editSupplier(id: Int){
        val jwt = preferences["jwt", ""]
        val call = apiService.editSupplier("Bearer $jwt", id)

        call.enqueue(object: Callback<Supplier> {
            override fun onResponse(call: Call<Supplier>, response: Response<Supplier>) {
                if (response.isSuccessful)
                {
                    val supplier = response.body()
                    if (supplier != null){

                        val message = "ID: ${supplier.id}\n" +
                                "Nacionalidad: ${supplier.nacionality}\n" +
                                "NIC/RUC: ${supplier.nic_ruc}\n" +
                                "Estado: ${supplier.state}\n" +
                                "Creado en: ${supplier.created_at}\n" +
                                "user en: ${supplier.user.name}\n" +
                                "email en: ${supplier.user.email}\n" +
                                "Actualizado en: ${supplier.updated_at}"

                        // Mostrar el mensaje en el registro (logcat)
                        Log.d("SupplierEdit", message)

                        val intent = Intent(this@SuppliersActivity, FormSupplierActivity::class.java)
                        intent.putExtra("supplier", supplier)
                        intent.putExtra("isEditing", true)
                        startActivity(intent)
                        finish()
                    }
                }
            }

            override fun onFailure(call: Call<Supplier>, t: Throwable) {
                t.localizedMessage?.let { toast(it) }
            }

        })
    }

    private fun loadSuppliers() {
        val jwt = preferences["jwt", ""]
        val call = apiService.getSuppliers("Bearer $jwt")
        val pbSuppliers = findViewById<ProgressBar>(R.id.pbSuppliers)
        val llContent = findViewById<LinearLayout>(R.id.llContent)

        pbSuppliers.visibility = View.VISIBLE
        llContent.visibility = View.GONE
        call.enqueue(object : Callback<ArrayList<Supplier>> {
            override fun onResponse(call: Call<ArrayList<Supplier>>, response: Response<ArrayList<Supplier>>) {
                if (response.isSuccessful) {

                    pbSuppliers.visibility = View.GONE
                    llContent.visibility = View.VISIBLE

                    response.body()?.let {
                        supplierAdapter.updateSuppliers(it)
                    }
                }
            }
            override fun onFailure(call: Call<ArrayList<Supplier>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this@SuppliersActivity, message, Toast.LENGTH_SHORT).show()
    }
}