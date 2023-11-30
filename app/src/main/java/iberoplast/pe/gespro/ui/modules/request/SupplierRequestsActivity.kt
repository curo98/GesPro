package iberoplast.pe.gespro.ui.modules.request

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
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import iberoplast.pe.gespro.R
import iberoplast.pe.gespro.io.ApiService
import iberoplast.pe.gespro.model.SupplierRequest
import iberoplast.pe.gespro.ui.adapters.SupplierRequestAdapter
import iberoplast.pe.gespro.util.ActionBarUtils
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
        PreferenceHelper.defaultPrefs(this@SupplierRequestsActivity)
    }
    private lateinit var etSearch: EditText
    private lateinit var btnCreate: Button
    private lateinit var supplierRequestAdapter: SupplierRequestAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supplier_requests)

        ActionBarUtils.setCustomTitle(
            this,
            "Solicitudes de proveedores"
        )

        etSearch = findViewById(R.id.etSearch)

        val userRoleName = preferences["UserRolePreferences", ""]
        Log.d("UserRoleActivityRequests", "Rol del usuario: $userRoleName")

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

        btnCreate = findViewById(R.id.btnCreateRequest)
        if (userRoleName != "proveedor" && userRoleName != "invitado") {
            btnCreate.visibility = View.GONE
        }

        btnCreate.setOnClickListener {
            val intent = Intent(this, FormRequestSupplierActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)

        if (v?.id == R.id.rvSupplierRequests) {
            menu?.setHeaderTitle("Opciones")
            val inflater: MenuInflater = menuInflater
            inflater.inflate(R.menu.menu_options, menu)

            val receiveItem = menu?.findItem(R.id.receive)
            val validateItem = menu?.findItem(R.id.validate)
            val showItem = menu?.findItem(R.id.show)
            val editItem = menu?.findItem(R.id.edit)
            val cancel = menu?.findItem(R.id.cancel)
            val approveItem = menu?.findItem(R.id.approve)
            val disapproveItem = menu?.findItem(R.id.disapprove)

            // Ocultar todos los elementos por defecto
            receiveItem?.isVisible = false
            validateItem?.isVisible = false
            showItem?.isVisible = false
            editItem?.isVisible = false
            cancel?.isVisible = false
            approveItem?.isVisible = false
            disapproveItem?.isVisible = false

            val selectedRequest = supplierRequestAdapter.selectedRequest
            val estado = selectedRequest?.getFinalState()?.name
            Log.d("EstadoSolicitud", "El estado de la solicitud es: $estado")

            val userRoleName = preferences["UserRolePreferences", ""]

            when (userRoleName) {
                "compras" -> {
                    showItem?.isVisible = true
                    receiveItem?.isVisible = estado == "Por recibir"
                    validateItem?.isVisible = estado == "Por validar"
                }

                "contabilidad" -> {
                    showItem?.isVisible = true
                    approveItem?.isVisible = estado == "Por aprobar"
                    disapproveItem?.isVisible =
                        estado != "Aprobada" && estado != "Cancelada" && estado != "Desaprobada" && estado == "Por aprobar"
                }

                "proveedor" -> {
                    showItem?.isVisible = true
                    editItem?.isVisible = true
                    cancel?.isVisible = true
                }

                "admin" -> {
                    showItem?.isVisible = true
                    editItem?.isVisible = estado != "Cancelada" && estado != "Desaprobada"
                    cancel?.isVisible =
                        estado != "Aprobada" && estado != "Cancelada" && estado != "Desaprobada"
                    receiveItem?.isVisible = estado == "Por recibir"
                    validateItem?.isVisible = estado == "Por validar"
                    approveItem?.isVisible = estado == "Por aprobar"
                    disapproveItem?.isVisible =
                        estado != "Aprobada" && estado != "Cancelada" && estado != "Desaprobada" && estado == "Por aprobar"
                }
                // Agrega más casos según sea necesario
            }
        }
    }


    override fun onContextItemSelected(item: MenuItem): Boolean {
        val selectedRequest = supplierRequestAdapter.selectedRequest
        val id = selectedRequest?.id

        when (item.itemId) {
            R.id.show -> executeIfNotNull(id) { showRequest(it) }
            R.id.edit -> executeIfNotNull(id) { editRequest(it) }
            R.id.receive -> executeIfNotNull(id) { receiveRequest(it) }
            R.id.validate -> executeIfNotNull(id) { validateRequest(it) }
            R.id.approve -> executeIfNotNull(id) { approveRequest(it) }
            R.id.cancel -> executeIfNotNull(id) { cancelRequest(it) }
            R.id.disapprove -> executeIfNotNull(id) { disapproveRequest(it) }
            else -> return super.onContextItemSelected(item)
        }

        return true
    }

    private inline fun executeIfNotNull(id: Int?, action: (Int) -> Unit) {
        id?.let { action(it) }
    }

    private fun performRequestAction(id: Int, action: String, successMessage: String) {
        val jwt = preferences["jwt", ""]
        val call = when (action) {
            "cancel" -> apiService.cancelRequest("Bearer $jwt", id)
            "disapprove" -> apiService.disapproveRequest("Bearer $jwt", id)
            "receive" -> apiService.receiveRequest("Bearer $jwt", id)
            "validate" -> apiService.validateRequest("Bearer $jwt", id)
            "approve" -> apiService.approveRequest("Bearer $jwt", id)
            else -> throw IllegalArgumentException("Acción no válida: $action")
        }

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    toast(successMessage)
                    val intent =
                        Intent(this@SupplierRequestsActivity, SupplierRequestsActivity::class.java)
                    finish()
                    startActivity(intent)
                } else {
                    toast("Problemas de permisos")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                t.localizedMessage?.let { toast(it) }
            }
        })
    }

    private fun cancelRequest(id: Int) {
        performRequestAction(id, "cancel", "La solicitud ha sido cancelada correctamente")
    }

    private fun disapproveRequest(id: Int) {
        performRequestAction(id, "disapprove", "Se ha desaprobado la solicitud correctamente")
    }

    private fun receiveRequest(id: Int) {
        performRequestAction(id, "receive", "Solicitud recibida correctamente")
    }

    private fun validateRequest(id: Int) {
        performRequestAction(id, "validate", "Solicitud validada correctamente")
    }

    private fun approveRequest(id: Int) {
        performRequestAction(id, "approve", "Solicitud aprobada correctamente")
    }


    private fun showRequest(id: Int) {
        val jwt = preferences["jwt", ""]
        val call = apiService.getRequestDetails("Bearer $jwt", id)

        call.enqueue(object : Callback<SupplierRequest> {
            override fun onResponse(
                call: Call<SupplierRequest>,
                response: Response<SupplierRequest>
            ) {
                if (response.isSuccessful) {
                    val request = response.body()

                    if (request != null) {

                        // Mostrar el mensaje en el registro (logcat)
                        Log.d("RequestDetails", request.toString())

                        val intent =
                            Intent(this@SupplierRequestsActivity, ShowRequestActivity::class.java)
                        intent.putExtra("request_details", request)
                        startActivity(intent)
                    }
                } else {
                    // Manejar la respuesta no exitosa, por ejemplo, mostrar un mensaje de error
                    toast("Error al obtener los detalles de la solicitud")
                }
            }

            override fun onFailure(call: Call<SupplierRequest>, t: Throwable) {
                // Manejar el error en la solicitud, por ejemplo, mostrar un mensaje de error
                toast("Error en la solicitud: ${t.message}")
            }
        })
    }

    private fun editRequest(id: Int) {
        val jwt = preferences["jwt", ""]
        val call = apiService.editRequest("Bearer $jwt", id)

        call.enqueue(object : Callback<SupplierRequest> {
            override fun onResponse(
                call: Call<SupplierRequest>,
                response: Response<SupplierRequest>
            ) {
                if (response.isSuccessful) {
                    val request = response.body()
                    if (request != null) {

                        val intent = Intent(
                            this@SupplierRequestsActivity,
                            FormRequestSupplierActivity::class.java
                        )
                        intent.putExtra("request", request)
                        intent.putExtra("isEditing", true)
                        startActivity(intent)
                        finish()
                    }
                }
            }

            override fun onFailure(call: Call<SupplierRequest>, t: Throwable) {
                t.localizedMessage?.let { toast(it) }
            }

        })
    }

    private fun loadSupplierRequests() {
        val jwt = preferences["jwt", ""]
        val call = apiService.getSupplierRequests("Bearer $jwt")
        val llLoader = findViewById<LinearLayout>(R.id.llLoader)
        val llContent = findViewById<LinearLayout>(R.id.llContent)

        llLoader.visibility = View.VISIBLE
        llContent.visibility = View.GONE
        call.enqueue(object : Callback<ArrayList<SupplierRequest>> {
            override fun onResponse(
                call: Call<ArrayList<SupplierRequest>>,
                response: Response<ArrayList<SupplierRequest>>
            ) {
                llLoader.visibility = View.GONE
                llContent.visibility = View.VISIBLE

                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.isEmpty()) {
                            toast("No hay solicitudes por mostrar")
                            finish()
                        } else {
                            // Actualizar el adaptador con las solicitudes
                            supplierRequestAdapter.updateRequests(it)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<SupplierRequest>>, t: Throwable) {
                toast("Error en la solicitud: ${t.message}")
            }
        })
    }
}

