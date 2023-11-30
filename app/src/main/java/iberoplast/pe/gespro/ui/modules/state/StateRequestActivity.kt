package iberoplast.pe.gespro.ui.modules.state

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
import iberoplast.pe.gespro.model.StateRequest
import iberoplast.pe.gespro.ui.adapters.StateRequestAdapter
import iberoplast.pe.gespro.util.ActionBarUtils
import iberoplast.pe.gespro.util.PreferenceHelper
import iberoplast.pe.gespro.util.PreferenceHelper.get
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StateRequestActivity : AppCompatActivity() {
    private val apiService: ApiService by lazy {
        ApiService.create()
    }
    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }
    private lateinit var stateRequestAdapter: StateRequestAdapter
    private lateinit var etSearch: EditText
    private lateinit var rvStates: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_state_request)

        ActionBarUtils.setCustomTitle(
            this,
            "Estados"
        )

        etSearch = findViewById(R.id.etSearch)
        rvStates = findViewById(R.id.rvStates)

        stateRequestAdapter = StateRequestAdapter()
        rvStates.layoutManager = LinearLayoutManager(this)
        rvStates.adapter = stateRequestAdapter

        registerForContextMenu(rvStates)
        rvStates.isLongClickable = true

        stateRequestAdapter.setNewSelectedStateRequest(null) // Inicializa el usuario seleccionado como nulo

        loadStates()

        val btnCreateState = findViewById<Button>(R.id.btnCreateState)
        btnCreateState.setOnClickListener {
            val intent = Intent(this, FormStateRequestActivity::class.java)
            startActivity(intent)
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No necesitas implementar esto
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString().toLowerCase()
                stateRequestAdapter.filterStateRequests(searchText)
            }

            override fun afterTextChanged(s: Editable?) {
                // No necesitas implementar esto
            }
        })
    }

    private fun loadStates() {
        val jwt = preferences["jwt", ""]
        val call = apiService.getStates("Bearer $jwt")
        val llLoader = findViewById<LinearLayout>(R.id.llLoader)
        val llContent = findViewById<LinearLayout>(R.id.llContent)
        llLoader.visibility = View.VISIBLE
        llContent.visibility = View.GONE
        call.enqueue(object : Callback<ArrayList<StateRequest>> {
            override fun onResponse(call: Call<ArrayList<StateRequest>>, response: Response<ArrayList<StateRequest>>) {
                llLoader.visibility = View.GONE
                llContent.visibility = View.VISIBLE
                if (response.isSuccessful) {
                    response.body()?.let {
                        stateRequestAdapter.updateStateRequests(it)
                    }
                }
            }
            override fun onFailure(call: Call<ArrayList<StateRequest>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)

        if (v?.id == R.id.rvStates) {
            menu?.setHeaderTitle("Opciones")
            val inflater: MenuInflater = menuInflater
            inflater.inflate(R.menu.menu_options_state, menu)
        }
    }
    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit -> {
                val selectedStateRequest = stateRequestAdapter.selectedStateRequest
                val id = selectedStateRequest?.id
                // Método a ejecutar para la opción R.id.edit
                if (id != null) {
                    editStateRequest(id)
                }
                return true
            }
            else -> return super.onContextItemSelected(item)
        }
    }

    private fun editStateRequest(id: Int){
        val jwt = preferences["jwt", ""]
        val call = apiService.editState("Bearer $jwt", id)

        call.enqueue(object: Callback<StateRequest> {
            override fun onResponse(call: Call<StateRequest>, response: Response<StateRequest>) {
                if (response.isSuccessful)
                {
                    val state = response.body()
                    if (state != null){

                        val intent = Intent(this@StateRequestActivity, FormStateRequestActivity::class.java)
                        intent.putExtra("state", state)
                        intent.putExtra("isEditing", true)
                        startActivity(intent)
                    }
                }
            }

            override fun onFailure(call: Call<StateRequest>, t: Throwable) {
            }

        })
    }
}