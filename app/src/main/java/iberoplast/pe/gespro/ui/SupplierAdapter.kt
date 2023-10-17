package iberoplast.pe.gespro.ui

//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.core.content.ContextCompat
//import androidx.recyclerview.widget.RecyclerView
//import iberoplast.pe.gespro.model.Supplier
//
//class SupplierAdapter(private val suppliers: ArrayList<Supplier>) : RecyclerView.Adapter<SupplierAdapter.ViewHolder>() {
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
//
//        fun bind(supplier: Supplier){
//            with(itemView){
//                val tvSupplierId = findViewById<TextView>(R.id.tvSupplierId)
//                val tvNameSupplier = findViewById<TextView>(R.id.tvNombreProveedor)
//                val tvEmailSupplier = findViewById<TextView>(R.id.tvCorreoProveedor)
//                val tvEstado = findViewById<TextView>(R.id.tvEstado)
//                tvSupplierId.text = itemView.context.getString(R.string.item_supplier_id, supplier.id)
//                tvNameSupplier.text = itemView.context.getString(R.string.item_name_supplier, supplier.nombreProveedor)
//                tvEmailSupplier.text = itemView.context.getString(R.string.item_email_supplier, supplier.emailProveedor)
//                tvEstado.text = itemView.context.getString(R.string.item_state_supplier, supplier.estado)
//
//                // Asignar colores según el estado
//                when (supplier.estado) {
//                    "Aprobado" -> {
//                        tvEstado.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorActivo))
//                    }
//                    "Rechazado" -> {
//                        tvEstado.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorInactivo))
//                    }
//                    "Por validar" -> {
//                        tvEstado.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorPendiente))
//                    }
//                }
//            }
//        }
//
//    }
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        return ViewHolder(
//            LayoutInflater.from(parent.context).inflate(R.layout.item_supplier, parent, false)
//        )
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//
//        val supplier = suppliers[position]
//
//        holder.bind(supplier)
//    }
//
//    override fun getItemCount(): Int {
//        return suppliers.size
//    }
//
//}

import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import iberoplast.pe.gespro.R
import iberoplast.pe.gespro.model.Supplier

class SupplierAdapter(private val suppliers: List<Supplier>) : RecyclerView.Adapter<SupplierAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_supplier, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val supplier = suppliers[position]
        holder.bind(supplier)
    }

    override fun getItemCount(): Int {
        return suppliers.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener {
        private val tvSupplierId: TextView = itemView.findViewById(R.id.tvSupplierId)
        private val tvNameSupplier: TextView = itemView.findViewById(R.id.tvNombreProveedor)
        private val tvEmailSupplier: TextView = itemView.findViewById(R.id.tvCorreoProveedor)
        private val tvEstado: TextView = itemView.findViewById(R.id.tvEstado)

        fun bind(supplier: Supplier) {
//            idTextView.text = supplier.nombreProveedor
//            nameTextView.text = supplier.nombreProveedor
//            emailTextView.text = supplier.emailProveedor
//            statusTextView.text = supplier.estado

            tvSupplierId.text = itemView.context.getString(R.string.item_supplier_id, supplier.id)
            tvNameSupplier.text = itemView.context.getString(R.string.item_name_supplier, supplier.nombreProveedor)
            tvEmailSupplier.text = itemView.context.getString(R.string.item_email_supplier, supplier.emailProveedor)
            tvEstado.text = itemView.context.getString(R.string.item_state_supplier, supplier.estado)
            when (supplier.estado) {
                "Aprobado" -> {
                    tvEstado.setTextColor(ContextCompat.getColor(itemView.context,
                        R.color.colorActivo
                    ))
                }
                "Rechazado" -> {
                    tvEstado.setTextColor(ContextCompat.getColor(itemView.context,
                        R.color.colorInactivo
                    ))
                }
                "Por validar" -> {
                    tvEstado.setTextColor(ContextCompat.getColor(itemView.context,
                        R.color.colorPendiente
                    ))
                }
            }
            itemView.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
            val inflater = MenuInflater(v?.context)
            inflater.inflate(R.menu.menu_options, menu)
        }
    }
}
