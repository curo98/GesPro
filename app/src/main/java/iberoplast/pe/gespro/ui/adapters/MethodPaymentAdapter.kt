package iberoplast.pe.gespro.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import iberoplast.pe.gespro.R
import iberoplast.pe.gespro.model.MethodPayment

class MethodPaymentAdapter : RecyclerView.Adapter<MethodPaymentAdapter.ViewHolder>() {
    private var methods_payments = ArrayList<MethodPayment>()
    private var filteredList = ArrayList<MethodPayment>()
    var selectedMethodPayment: MethodPayment? = null

    fun updateMethodsPayments(newMethodsPayments: List<MethodPayment>) {
        methods_payments.clear()
        methods_payments.addAll(newMethodsPayments)
        filterMethodsPayments("")
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterMethodsPayments(query: String) {
        filteredList.clear()

        if (query.isEmpty()) {
            filteredList.addAll(methods_payments)
        } else {
            for (method_payment in methods_payments) {
                if (method_payment.name.toLowerCase().contains(query)) {
                    filteredList.add(method_payment)
                }
            }
        }

        notifyDataSetChanged()
    }

    // Renombrar la función que utiliza el operador de asignación personalizado
    fun setNewSelectedMethodPayment(method_payment: MethodPayment?) {
        selectedMethodPayment = method_payment
    }

    fun getPosition(method_payment: MethodPayment?): Int {
        method_payment?.let {
            for (index in 0 until methods_payments.size) {
                if (methods_payments[index] == method_payment) {
                    return index
                }
            }
        }
        return -1 // Rol no encontrado en la lista
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvMethodName: TextView = itemView.findViewById(R.id.tvMethodName)
        private val tvMethodDescription: TextView = itemView.findViewById(R.id.tvMethodDescription)

        fun bind(method_payment: MethodPayment) {
            tvMethodName.text = method_payment.name
            tvMethodDescription.text = method_payment.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_method_payment, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val method_payment = filteredList[position]

        holder.bind(method_payment)
        // Agregar OnLongClickListener a la vista raíz del elemento
        holder.itemView.setOnLongClickListener { view ->
            setNewSelectedMethodPayment(method_payment)
            // Muestra el menú contextual
            view.showContextMenu()
            true
        }
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

}