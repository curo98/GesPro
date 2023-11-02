package iberoplast.pe.gespro.ui.adapters

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import iberoplast.pe.gespro.R
import iberoplast.pe.gespro.model.SupplierRequest

class SupplierRequestAdapter(private val supplierRequests: List<SupplierRequest>) : RecyclerView.Adapter<SupplierRequestAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_supplier_request, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val supplier = supplierRequests[position]
        holder.bind(supplier)
    }

    override fun getItemCount(): Int {
        return supplierRequests.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener {
        private val tvRequestId: TextView = itemView.findViewById(R.id.tvRequestId)
        private val tvUserId: TextView = itemView.findViewById(R.id.tvUserId)
        private val tvStateId: TextView = itemView.findViewById(R.id.tvStateId)
        private val tvTypePayId: TextView = itemView.findViewById(R.id.tvTypePayId)
        private val tvMethodPayId: TextView = itemView.findViewById(R.id.tvMethodPayId)

        fun bind(request: SupplierRequest) {

            // CODIGO PARA APLICAR NEGRITA A LOS TEXTVIEW, EXCEPTO A LOS VALOR OBTENIDOS DESDE LA API
            applyBoldStyleToAttribute(tvRequestId, "Solicitud #", request.id)
            applyBoldStyleToAttribute(tvUserId, "Solicitante", request.user.name)
            val finalState = request.getFinalState()
            if (finalState != null) {
                when (finalState.name) {
                    "Aprobado" -> {
                        tvStateId.text = finalState.name
                        tvStateId.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorActivo))
                    }
                    "Rechazado" -> {
                        tvStateId.text = finalState.name
                        tvStateId.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorInactivo))
                    }
                    "Por validar" -> {
                        tvStateId.text = finalState.name
                        tvStateId.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorPendiente))
                    }
                    else -> {
                        tvStateId.text = finalState.name
                        tvStateId.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorPrimaryDark)) // Establece un color predeterminado si no coincide con los casos anteriores
                    }
                }
            } else {
                // No hay transiciones de estado, maneja esto como desees
                tvStateId.text = "Sin transiciones"
                tvStateId.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorPrimaryDark)) // Establece un color predeterminado si no hay transiciones
            }

            applyBoldStyleToAttribute(tvTypePayId, "Comprobante emitido", request.type_payment.name)
            applyBoldStyleToAttribute(tvMethodPayId, "Pago por", request.method_payment.name)

            itemView.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
            val inflater = MenuInflater(v?.context)
            inflater.inflate(R.menu.menu_options, menu)
        }

        private fun applyBoldStyleToAttribute(textView: TextView, attribute: String, value: Any?) {

            val text = if (value == "Aprobado" || value == "Rechazado" || value == "Por validar") {
                    "$value"
                } else {
                    "$attribute: $value"
                }

            val spannableString = SpannableString(text)
            spannableString.setSpan(StyleSpan(Typeface.BOLD), 0, attribute.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            textView.text = spannableString
        }
    }
}
