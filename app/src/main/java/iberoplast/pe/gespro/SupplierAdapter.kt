package iberoplast.pe.gespro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import iberoplast.pe.gespro.model.Supplier

class SupplierAdapter(private val suppliers: ArrayList<Supplier>) : RecyclerView.Adapter<SupplierAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bind(supplier: Supplier){
            with(itemView){
                val tvSupplierId = findViewById<TextView>(R.id.tvSupplierId)
                val tvNameSupplier = findViewById<TextView>(R.id.tvNombreProveedor)
                val tvEmailSupplier = findViewById<TextView>(R.id.tvCorreoProveedor)
                tvSupplierId.text = itemView.context.getString(R.string.item_supplier_id, supplier.id)
                tvNameSupplier.text = itemView.context.getString(R.string.item_name_supplier, supplier.nombreProveedor)
                tvEmailSupplier.text = itemView.context.getString(R.string.item_email_supplier, supplier.emailProveedor)
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_supplier, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val supplier = suppliers[position]

        holder.bind(supplier)
    }

    override fun getItemCount(): Int {
        return suppliers.size
    }

}