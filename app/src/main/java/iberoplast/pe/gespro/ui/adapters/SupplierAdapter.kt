package iberoplast.pe.gespro.ui.adapters

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import iberoplast.pe.gespro.R
import iberoplast.pe.gespro.model.Supplier

class SupplierAdapter : RecyclerView.Adapter<SupplierAdapter.ViewHolder>() {
    private var suppliers = ArrayList<Supplier>()
    private var filteredList = ArrayList<Supplier>()
    var selectedSupplier: Supplier? = null

    fun updateSuppliers(newSuppliers: List<Supplier>) {
        suppliers.clear()
        suppliers.addAll(newSuppliers)
        filterSuppliers("")
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterSuppliers(query: String) {
        filteredList.clear()

        if (query.isEmpty()) {
            filteredList.addAll(suppliers)
        } else {
            for (supplier in suppliers) {
                if (supplier.id.toString().toLowerCase().contains(query)) {
                    filteredList.add(supplier)
                }
            }
        }

        notifyDataSetChanged()
    }

    // Renombrar la función que utiliza el operador de asignación personalizado
    fun setNewSelectedSupplier(supplier: Supplier?) {
        selectedSupplier = supplier
    }

    fun getPosition(supplier: Supplier?): Int {
        supplier?.let {
            for (index in 0 until suppliers.size) {
                if (suppliers[index] == supplier) {
                    return index
                }
            }
        }
        return -1 // Usuario no encontrado en la lista
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvSupplierId: TextView = itemView.findViewById(R.id.tvSupplierId)
        private val tvNameSupplier: TextView = itemView.findViewById(R.id.tvNameSupplier)
        private val tvNacionality: TextView = itemView.findViewById(R.id.tvNacionality)
        private val tvEmail: TextView = itemView.findViewById(R.id.tvEmail)
        private val tvState: TextView = itemView.findViewById(R.id.tvState)

        fun bind(supplier: Supplier) {

            // CODIGO PARA APLICAR NEGRITA A LOS TEXTVIEW, EXCEPTO A LOS VALOR OBTENIDOS DESDE LA API
            applyBoldStyleToAttribute(tvSupplierId, "Solicitud #", supplier.id)
            applyBoldStyleToAttribute(tvNameSupplier, "Nombre", supplier.user.name)
            applyBoldStyleToAttribute(tvNacionality, "Nacionalidad", supplier.nacionality)
            applyBoldStyleToAttribute(tvEmail, "E-mail:", supplier.user.email)
            applyBoldStyleToAttribute(tvState, "", supplier.state)

            when (supplier.state) {
                "inactivo" -> {
                    tvState.setTextColor(
                        ContextCompat.getColor(itemView.context,
                            R.color.colorInactivo
                        ))
                }
                "activo" -> {
                    tvState.setTextColor(
                        ContextCompat.getColor(itemView.context,
                            R.color.colorActivo
                        ))
                }
            }
        }
        private fun applyBoldStyleToAttribute(textView: TextView, attribute: String, value: Any?) {
//
            val text = if (value == "activo" || value == "inactivo") {
                "$value"
            } else {
                "$attribute: $value"
            }

            val spannableString = SpannableString(text)
            spannableString.setSpan(StyleSpan(Typeface.BOLD), 0, attribute.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            textView.text = spannableString
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_supplier, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val supplier = filteredList[position]

        holder.bind(supplier)
        // Agregar OnLongClickListener a la vista raíz del elemento
        holder.itemView.setOnLongClickListener { view ->
            setNewSelectedSupplier(supplier)
            // Muestra el menú contextual
            view.showContextMenu()
            true
        }
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

}