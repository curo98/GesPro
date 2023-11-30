package iberoplast.pe.gespro.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import iberoplast.pe.gespro.R
import iberoplast.pe.gespro.model.TypePayment

class ConditionPaymentAdapter : RecyclerView.Adapter<ConditionPaymentAdapter.ViewHolder>() {
    private var conditions_payments = ArrayList<TypePayment>()
    private var filteredList = ArrayList<TypePayment>()
    var selectedConditionPayment: TypePayment? = null

    fun updateConditionsPayments(newConditionsPayments: List<TypePayment>) {
        conditions_payments.clear()
        conditions_payments.addAll(newConditionsPayments)
        filterConditionsPayments("")
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterConditionsPayments(query: String) {
        filteredList.clear()

        if (query.isEmpty()) {
            filteredList.addAll(conditions_payments)
        } else {
            for (condition_payment in conditions_payments) {
                if (condition_payment.name.toLowerCase().contains(query)) {
                    filteredList.add(condition_payment)
                }
            }
        }

        notifyDataSetChanged()
    }

    // Renombrar la función que utiliza el operador de asignación personalizado
    fun setNewSelectedConditionPayment(condition_payment: TypePayment?) {
        selectedConditionPayment = condition_payment
    }

    fun getPosition(condition_payment: TypePayment?): Int {
        condition_payment?.let {
            for (index in 0 until conditions_payments.size) {
                if (conditions_payments[index] == condition_payment) {
                    return index
                }
            }
        }
        return -1 // Rol no encontrado en la lista
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvConditionName: TextView = itemView.findViewById(R.id.tvConditionName)
        private val tvConditionDescription: TextView = itemView.findViewById(R.id.tvConditionDescription)

        fun bind(condition_payment: TypePayment) {
            tvConditionName.text = condition_payment.name
            tvConditionDescription.text = condition_payment.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_condition_payment, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val condition_payment = filteredList[position]

        holder.bind(condition_payment)
        // Agregar OnLongClickListener a la vista raíz del elemento
        holder.itemView.setOnLongClickListener { view ->
            setNewSelectedConditionPayment(condition_payment)
            // Muestra el menú contextual
            view.showContextMenu()
            true
        }
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

}