package iberoplast.pe.gespro.ui.adapters

import Role
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import iberoplast.pe.gespro.R

class RoleAdapter : RecyclerView.Adapter<RoleAdapter.ViewHolder>() {
    private var roles = ArrayList<Role>()
    private var filteredList = ArrayList<Role>()
    var selectedRole: Role? = null

    fun updateRoles(newRoles: List<Role>) {
        roles.clear()
        roles.addAll(newRoles)
        filterRoles("")
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterRoles(query: String) {
        filteredList.clear()

        if (query.isEmpty()) {
            filteredList.addAll(roles)
        } else {
            for (role in roles) {
                if (role.name.toLowerCase().contains(query)) {
                    filteredList.add(role)
                }
            }
        }

        notifyDataSetChanged()
    }

    // Renombrar la función que utiliza el operador de asignación personalizado
    fun setNewSelectedRole(role: Role?) {
        selectedRole = role
    }

    fun getPosition(role: Role?): Int {
        role?.let {
            for (index in 0 until roles.size) {
                if (roles[index] == role) {
                    return index
                }
            }
        }
        return -1 // Rol no encontrado en la lista
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvRoleName: TextView = itemView.findViewById(R.id.tvRoleName)
        private val tvRoleDescription: TextView = itemView.findViewById(R.id.tvRoleDescription)

        fun bind(role: Role) {
            tvRoleName.text = role.name
            tvRoleDescription.text = role.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_role, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val role = filteredList[position]

        holder.bind(role)
        // Agregar OnLongClickListener a la vista raíz del elemento
        holder.itemView.setOnLongClickListener { view ->
            setNewSelectedRole(role)
            // Muestra el menú contextual
            view.showContextMenu()
            true
        }
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

}