package iberoplast.pe.gespro.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import iberoplast.pe.gespro.R
import iberoplast.pe.gespro.model.Supplier

class SupplierAdapter : RecyclerView.Adapter<SupplierAdapter.ViewHolder>() {
    private var suppliers = ArrayList<Supplier>()
    var selectedSupplier: Supplier? = null

    // Renombrar la función que utiliza el operador de asignación personalizado
    fun setNewSelectedSupplier(supplier: Supplier?) {
        selectedSupplier = supplier
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(supplier: Supplier) {
            with(itemView) {
                val tvSupplierId = findViewById<TextView>(R.id.tvSupplierId)
                val tvNameSupplier = findViewById<TextView>(R.id.tvNameSupplier)
                val tvNacionality = findViewById<TextView>(R.id.tvNacionality)
                val tvEmailSupplier = findViewById<TextView>(R.id.tvEmail)
                val tvEstado = findViewById<TextView>(R.id.tvState)
                tvSupplierId.text =
                    itemView.context.getString(R.string.item_supplier_id, supplier.id)
                tvNameSupplier.text =
                    itemView.context.getString(R.string.item_name_supplier, supplier.user.name)
                tvNacionality.text =
                    itemView.context.getString(R.string.item_name_supplier, supplier.nacionality)
                tvEmailSupplier.text =
                    itemView.context.getString(R.string.item_email_supplier, supplier.user.email)
                tvEstado.text =
                    itemView.context.getString(R.string.item_state_supplier, supplier.state)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_supplier, parent, false)
        )
    }
    fun updateSuppliers(newSuppliers: List<Supplier>) {
        suppliers.clear()
        suppliers.addAll(newSuppliers)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val supplier = suppliers[position]

        holder.bind(supplier)
        // Agregar OnLongClickListener a la vista raíz del elemento
        holder.itemView.setOnLongClickListener { view ->
            // Muestra el menú contextual
            view.showContextMenu()
            setNewSelectedSupplier(supplier)
            true
        }
    }

    override fun getItemCount(): Int {
        return suppliers.size
    }

}

//import android.graphics.Typeface
//import android.text.Spannable
//import android.text.SpannableString
//import android.text.style.StyleSpan
//import android.view.ContextMenu
//import android.view.LayoutInflater
//import android.view.MenuInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.core.content.ContextCompat
//import androidx.recyclerview.widget.RecyclerView
//import iberoplast.pe.gespro.R
//import iberoplast.pe.gespro.model.Supplier
//
//class SupplierAdapter(private val suppliers: List<Supplier>) : RecyclerView.Adapter<SupplierAdapter.ViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_supplier, parent, false)
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val supplier = suppliers[position]
//        holder.bind(supplier)
//    }
//
//    override fun getItemCount(): Int {
//        return suppliers.size
//    }
//
//    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener {
//        private val tvSupplierId: TextView = itemView.findViewById(R.id.tvSupplierId)
//        private val tvNameSupplier: TextView = itemView.findViewById(R.id.tvNameSupplier)
//        private val tvNacionality: TextView = itemView.findViewById(R.id.tvNacionality)
//        private val tvEmail: TextView = itemView.findViewById(R.id.tvEmail)
//        private val tvState: TextView = itemView.findViewById(R.id.tvState)
//
//        fun bind(request: Supplier) {
//
//            // CODIGO PARA APLICAR NEGRITA A LOS TEXTVIEW, EXCEPTO A LOS VALOR OBTENIDOS DESDE LA API
//            applyBoldStyleToAttribute(tvSupplierId, "Solicitud #", request.id)
//            applyBoldStyleToAttribute(tvNameSupplier, "Nombre", request.user.name)
//            applyBoldStyleToAttribute(tvNacionality, "Nacionalidad", request.nacionality)
//            applyBoldStyleToAttribute(tvEmail, "E-mail:", request.user.email)
//            applyBoldStyleToAttribute(tvState, "", request.state)
//
//            when (request.state) {
//                "inactivo" -> {
//                    tvState.setTextColor(
//                        ContextCompat.getColor(itemView.context,
//                            R.color.colorInactivo
//                        ))
//                }
//                "activo" -> {
//                    tvState.setTextColor(
//                        ContextCompat.getColor(itemView.context,
//                            R.color.colorActivo
//                        ))
//                }
//            }
//            itemView.setOnCreateContextMenuListener(this)
//        }
//
//        override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
//            val inflater = MenuInflater(v?.context)
//            inflater.inflate(R.menu.menu_options, menu)
//        }
//
//        private fun applyBoldStyleToAttribute(textView: TextView, attribute: String, value: Any?) {
//
//            val text = if (value == "activo" || value == "inactivo") {
//                "$value"
//            } else {
//                "$attribute: $value"
//            }
//
//            val spannableString = SpannableString(text)
//            spannableString.setSpan(StyleSpan(Typeface.BOLD), 0, attribute.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
//            textView.text = spannableString
//        }
//    }
//}
