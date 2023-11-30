package iberoplast.pe.gespro.ui.modules.role

import Role
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
import iberoplast.pe.gespro.ui.adapters.RoleAdapter
import iberoplast.pe.gespro.util.ActionBarUtils
import iberoplast.pe.gespro.util.PreferenceHelper
import iberoplast.pe.gespro.util.PreferenceHelper.get
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RoleActivity : AppCompatActivity() {
    private val apiService: ApiService by lazy {
        ApiService.create()
    }
    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }
    private lateinit var roleAdapter: RoleAdapter
    private lateinit var etSearch: EditText
    private lateinit var rvRoles: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_role)

        ActionBarUtils.setCustomTitle(
            this,
            "Roles de usuarios"
        )

        etSearch = findViewById(R.id.etSearch)
        rvRoles = findViewById(R.id.rvRoles)

        roleAdapter = RoleAdapter()
        rvRoles.layoutManager = LinearLayoutManager(this)
        rvRoles.adapter = roleAdapter

        registerForContextMenu(rvRoles)
        rvRoles.isLongClickable = true

        roleAdapter.setNewSelectedRole(null) // Inicializa el usuario seleccionado como nulo

        loadRoles()

        val btnCreateRole = findViewById<Button>(R.id.btnCreateRole)
        btnCreateRole.setOnClickListener {
            val intent = Intent(this, FormRoleActivity::class.java)
            startActivity(intent)
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No necesitas implementar esto
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString().toLowerCase()
                roleAdapter.filterRoles(searchText)
            }

            override fun afterTextChanged(s: Editable?) {
                // No necesitas implementar esto
            }
        })
    }

    private fun loadRoles() {
        val jwt = preferences["jwt", ""]
        val call = apiService.getRoles("Bearer $jwt")
        val llLoader = findViewById<LinearLayout>(R.id.llLoader)
        val llContent = findViewById<LinearLayout>(R.id.llContent)
        llLoader.visibility = View.VISIBLE
        llContent.visibility = View.GONE
        call.enqueue(object : Callback<ArrayList<Role>> {
            override fun onResponse(call: Call<ArrayList<Role>>, response: Response<ArrayList<Role>>) {
                llLoader.visibility = View.GONE
                llContent.visibility = View.VISIBLE
                if (response.isSuccessful) {
                    response.body()?.let {
                        roleAdapter.updateRoles(it)
                    }
                }
            }
            override fun onFailure(call: Call<ArrayList<Role>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)

        if (v?.id == R.id.rvRoles) {
            menu?.setHeaderTitle("Opciones")
            val inflater: MenuInflater = menuInflater
            inflater.inflate(R.menu.menu_options_role, menu)
        }
    }
    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit -> {
                val selectedRole = roleAdapter.selectedRole
                val id = selectedRole?.id
                // Método a ejecutar para la opción R.id.edit
                if (id != null) {
                    editRole(id)
                }
                return true
            }
            else -> return super.onContextItemSelected(item)
        }
    }

    private fun editRole(id: Int){
        val jwt = preferences["jwt", ""]
        val call = apiService.editRole("Bearer $jwt", id)

        call.enqueue(object: Callback<Role> {
            override fun onResponse(call: Call<Role>, response: Response<Role>) {
                if (response.isSuccessful)
                {
                    val role = response.body()
                    if (role != null){

                        val intent = Intent(this@RoleActivity, FormRoleActivity::class.java)
                        intent.putExtra("role", role)
                        intent.putExtra("isEditing", true)
                        startActivity(intent)
                    }
                }
            }

            override fun onFailure(call: Call<Role>, t: Throwable) {
            }

        })
    }
}