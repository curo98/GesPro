package iberoplast.pe.gespro.ui.modules.user

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
import iberoplast.pe.gespro.model.User
import iberoplast.pe.gespro.ui.adapters.UserAdapter
import iberoplast.pe.gespro.util.ActionBarUtils
import iberoplast.pe.gespro.util.PreferenceHelper
import iberoplast.pe.gespro.util.PreferenceHelper.get
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserActivity : AppCompatActivity() {
    private val apiService: ApiService by lazy {
        ApiService.create()
    }
    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }
    private lateinit var userAdapter: UserAdapter
    private lateinit var etSearch: EditText
    private lateinit var rvUsers: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        ActionBarUtils.setCustomTitle(
            this,
            "Usuarios"
        )

        etSearch = findViewById(R.id.etSearch)
        rvUsers = findViewById(R.id.rvUsers)

        userAdapter = UserAdapter()
        rvUsers.layoutManager = LinearLayoutManager(this)
        rvUsers.adapter = userAdapter

        registerForContextMenu(rvUsers)
        rvUsers.isLongClickable = true

        userAdapter.setNewSelectedUser(null) // Inicializa el usuario seleccionado como nulo

        loadUsers()

        val btnCreateUser = findViewById<Button>(R.id.btnCreateUser)
        btnCreateUser.setOnClickListener {
            val intent = Intent(this, FormUserActivity::class.java)
            startActivity(intent)
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No necesitas implementar esto
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString().toLowerCase()
                userAdapter.filterUsers(searchText)
            }

            override fun afterTextChanged(s: Editable?) {
                // No necesitas implementar esto
            }
        })
    }

    private fun loadUsers() {
        val jwt = preferences["jwt", ""]
        val call = apiService.getUsers("Bearer $jwt")
        val llLoader = findViewById<LinearLayout>(R.id.llLoader)
        val llContent = findViewById<LinearLayout>(R.id.llContent)
        llLoader.visibility = View.VISIBLE
        llContent.visibility = View.GONE
        call.enqueue(object : Callback<ArrayList<User>> {
            override fun onResponse(call: Call<ArrayList<User>>, response: Response<ArrayList<User>>) {
                llLoader.visibility = View.GONE
                llContent.visibility = View.VISIBLE
                if (response.isSuccessful) {
                    response.body()?.let {
                        userAdapter.updateUsers(it)
                    }
                }
            }
            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)

        if (v?.id == R.id.rvUsers) {
            menu?.setHeaderTitle("Opciones")
            val inflater: MenuInflater = menuInflater
            inflater.inflate(R.menu.menu_options_user, menu)
        }
    }
    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit -> {
                val selectedUser = userAdapter.selectedUser
                val id = selectedUser?.id
                // Método a ejecutar para la opción R.id.edit
                if (id != null) {
                    editUser(id)
                }
                return true
            }
            else -> return super.onContextItemSelected(item)
        }
    }

    private fun editUser(id: Int){
        val jwt = preferences["jwt", ""]
        val call = apiService.editUser("Bearer $jwt", id)

        call.enqueue(object: Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful)
                {
                    val user = response.body()
                    if (user != null){

                        val intent = Intent(this@UserActivity, FormUserActivity::class.java)
                        intent.putExtra("user", user)
                        intent.putExtra("isEditing", true)
                        startActivity(intent)
                    }
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
            }

        })
    }
}