package iberoplast.pe.gespro.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import iberoplast.pe.gespro.R
import iberoplast.pe.gespro.model.StateRequest

class StateRequestAdapter : RecyclerView.Adapter<StateRequestAdapter.ViewHolder>() {
    private var states = ArrayList<StateRequest>()
    private var filteredList = ArrayList<StateRequest>()
    var selectedStateRequest: StateRequest? = null

    fun updateStateRequests(newStateRequests: List<StateRequest>) {
        states.clear()
        states.addAll(newStateRequests)
        filterStateRequests("")
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterStateRequests(query: String) {
        filteredList.clear()

        if (query.isEmpty()) {
            filteredList.addAll(states)
        } else {
            for (state in states) {
                if (state.name.toLowerCase().contains(query)) {
                    filteredList.add(state)
                }
            }
        }

        notifyDataSetChanged()
    }

    // Renombrar la función que utiliza el operador de asignación personalizado
    fun setNewSelectedStateRequest(state: StateRequest?) {
        selectedStateRequest = state
    }

    fun getPosition(state: StateRequest?): Int {
        state?.let {
            for (index in 0 until states.size) {
                if (states[index] == state) {
                    return index
                }
            }
        }
        return -1 // Rol no encontrado en la lista
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvState: TextView = itemView.findViewById(R.id.tvState)
        private val tvDescriptionState: TextView = itemView.findViewById(R.id.tvDescriptionState)

        fun bind(state: StateRequest) {
            tvState.text = state.name
            tvDescriptionState.text = state.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_state, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val state = filteredList[position]

        holder.bind(state)
        // Agregar OnLongClickListener a la vista raíz del elemento
        holder.itemView.setOnLongClickListener { view ->
            setNewSelectedStateRequest(state)
            // Muestra el menú contextual
            view.showContextMenu()
            true
        }
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

}