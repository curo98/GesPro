package iberoplast.pe.gespro.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import iberoplast.pe.gespro.R
import iberoplast.pe.gespro.model.User

class UserAdapter : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    private var users = ArrayList<User>()
    private var filteredList = ArrayList<User>()
    var selectedUser: User? = null

    fun updateUsers(newUsers: List<User>) {
        users.clear()
        users.addAll(newUsers)
        filterUsers("")
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterUsers(query: String) {
        filteredList.clear()

        if (query.isEmpty()) {
            filteredList.addAll(users)
        } else {
            for (user in users) {
                if (user.name.toLowerCase().contains(query)) {
                    filteredList.add(user)
                }
            }
        }

        notifyDataSetChanged()
    }

    // Renombrar la función que utiliza el operador de asignación personalizado
    fun setNewSelectedUser(user: User?) {
        selectedUser = user
    }

    fun getPosition(user: User?): Int {
        user?.let {
            for (index in 0 until users.size) {
                if (users[index] == user) {
                    return index
                }
            }
        }
        return -1 // Rol no encontrado en la lista
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(R.id.tvName)
        private val tvEmail: TextView = itemView.findViewById(R.id.tvEmail)
        private val tvRole: TextView = itemView.findViewById(R.id.tvRole)

        fun bind(user: User) {
            tvName.text = user.name
            tvEmail.text = user.email
            tvRole.text = user.role?.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val user = filteredList[position]

        holder.bind(user)
        // Agregar OnLongClickListener a la vista raíz del elemento
        holder.itemView.setOnLongClickListener { view ->
            setNewSelectedUser(user)
            // Muestra el menú contextual
            view.showContextMenu()
            true
        }
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

}