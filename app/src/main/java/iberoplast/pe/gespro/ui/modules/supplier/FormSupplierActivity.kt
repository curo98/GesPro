package iberoplast.pe.gespro.ui.modules.supplier

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import iberoplast.pe.gespro.R
import iberoplast.pe.gespro.io.ApiService
import iberoplast.pe.gespro.model.Countrie
import iberoplast.pe.gespro.model.Supplier
import iberoplast.pe.gespro.util.ActionBarUtils
import iberoplast.pe.gespro.util.PreferenceHelper
import iberoplast.pe.gespro.util.PreferenceHelper.get
import iberoplast.pe.gespro.util.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormSupplierActivity : AppCompatActivity() {
    private val apiService: ApiService by lazy {
        ApiService.create()
    }
    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }
    private val etName by lazy { findViewById<EditText>(R.id.etName) }
    private val etEmail by lazy { findViewById<EditText>(R.id.etEmail) }
    private val spCountry by lazy { findViewById<Spinner>(R.id.spCountry) }
    private val etNicRuc by lazy { findViewById<EditText>(R.id.etNicRuc) }
    private val btnForm by lazy { findViewById<Button>(R.id.btnFormSupplier) }
    private val btnGoToList by lazy { findViewById<Button>(R.id.btnGoToList) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_supplier)

        // Obtén el valor de "isEditing" del intent
        val isEditing = intent.getBooleanExtra("isEditing", false)
        val supplier = intent.getParcelableExtra<Supplier>("supplier")

        // Si isEditing es true, estás editando un proveedor, muestra los datos existentes
        if (isEditing && supplier != null) {
            ActionBarUtils.setCustomTitle(
                this,
                "Editando proveedor:",
                "${supplier.user.name}"
            )
            etName.setText(supplier.user.name)
            etNicRuc.setText(supplier.nic_ruc)
            etEmail.setText(supplier.user.email)

            // Cambia el nombre del botón a "Actualizar"
            btnForm.text = "Actualizar"
            btnForm.setOnClickListener {
                val id = supplier.id
                // Ejecuta el método de edición
                executeMethodUpdate(id)
            }
        } else {
            ActionBarUtils.setCustomTitle(
                this,
                "Registro de nuevo proveedor"
            )
            // Si no estás editando, estás creando un nuevo proveedor, configura el botón
            // para ejecutar el método de creación
            btnForm.text = "Crear"
//            val btnFormC = findViewById<Button>(R.id.btnFormSupplier)
            btnForm.setOnClickListener {
                // Llama a tu método de creación aquí
                executeMethodCreate()
            }
        }

        btnGoToList.setOnClickListener {
            val intent = Intent(this, SuppliersActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Cargar la lista de países en el Spinner y seleccionar "Venezuela" si está definido
        loadCountries(spCountry, supplier)
    }

    private fun loadCountries(spCountry: Spinner, supplier: Supplier?) {
        val call = apiService.getCountries()
        call.enqueue(object : Callback<ArrayList<Countrie>> {
            override fun onResponse(
                call: Call<ArrayList<Countrie>>,
                response: Response<ArrayList<Countrie>>
            ) {
                if (response.isSuccessful) {
                    val countries = response.body() ?: ArrayList() // Convierte a una lista

                    // Extrae los nombres de los países y crea un ArrayAdapter
                    val countryNames = countries.map { it.name }
                    val adapter = ArrayAdapter(
                        this@FormSupplierActivity,
                        android.R.layout.simple_spinner_item,
                        countryNames
                    )

                    // Establece el estilo del Spinner
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    // Asigna el ArrayAdapter al Spinner
                    spCountry.adapter = adapter

                    // Si `supplier` no es nulo, intenta seleccionar la nacionalidad (country) en el Spinner
                    supplier?.nacionality?.let { selectedCountry ->
                        val selectedIndex = countryNames.indexOf(selectedCountry)
                        if (selectedIndex != -1) {
                            spCountry.setSelection(selectedIndex)
                        }
                    } ?: run {
                        // Si `supplier` es nulo o `nacionality` es nulo, selecciona "Perú" por defecto
                        val peruIndex = countryNames.indexOf("Perú")
                        if (peruIndex != -1) {
                            spCountry.setSelection(peruIndex)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<Countrie>>, t: Throwable) {
                // Manejo de errores en caso de fallo en la solicitud
                t.localizedMessage?.let { toast(it) }
            }
        })
    }


    private fun executeMethodUpdate(id: Int) {
        val name = etName.text.toString().trim()
        val nic_ruc = etNicRuc.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val jwt = preferences["jwt", ""]
        val authHeader = "Bearer $jwt"

        val call = apiService.updateSupplier(authHeader, id, name, nic_ruc, email)
        call.enqueue(object: Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    toast("Se ha actualizado los datos correctamente")
                    val intent = Intent(this@FormSupplierActivity, SuppliersActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Manejo de errores
                    val errorMessage = when (response.code()) {
                        401 -> "No autorizado"
                        404 -> "Proveedor no encontrado"
                        500 -> "Error interno del servidor"
                        else -> "Error desconocido"
                    }
                    toast(errorMessage)
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                t.localizedMessage?.let { toast(it) }
            }
        })
    }

    private fun executeMethodCreate()
    {
        val name = etName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val nic_ruc = etNicRuc.text.toString().trim()
        val nacionality = spCountry.selectedItem.toString() // Obtener el país seleccionado

        val jwt = preferences["jwt", ""]
        val authHeader = "Bearer $jwt"

        val call = apiService.storeSupplier(authHeader, name, email, nic_ruc, "", "", nacionality)
        call.enqueue(object: Callback<Supplier>{
            override fun onResponse(call: Call<Supplier>, response: Response<Supplier>) {
                if (response.isSuccessful) {
                    toast("Se ha creado al proveedor correctamente")
                    val intent = Intent(this@FormSupplierActivity, SuppliersActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Manejo de errores
                    toast("Error al crear el nuevo proveedor")
                }
            }

            override fun onFailure(call: Call<Supplier>, t: Throwable) {
                toast(t.localizedMessage)
            }

        })
    }
}