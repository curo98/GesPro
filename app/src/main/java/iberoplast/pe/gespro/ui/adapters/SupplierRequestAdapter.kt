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
//            tvRequestId.text = itemView.context.getString(R.string.item_request_id, request.id)
//            tvUserId.text = itemView.context.getString(R.string.item_user_request, request.user.name)
//            tvStateId.text = itemView.context.getString(R.string.item_state_request, request.state.name)
//            tvTypePayId.text = itemView.context.getString(R.string.item_typePayment_request, request.type_payment.name)
//            tvMethodPayId.text = itemView.context.getString(R.string.item_methodPayment_request, request.method_payment.name)

            // CODIGO PARA APLICAR NEGRITA A LOS TEXTVIEW, EXCEPTO A LOS VALOR OBTENIDOS DESDE LA API
            applyBoldStyleToAttribute(tvRequestId, "Solicitud #", request.id)
            applyBoldStyleToAttribute(tvUserId, "Solicitante", request.user.name)
            applyBoldStyleToAttribute(tvStateId, "", request.state.name)
            applyBoldStyleToAttribute(tvTypePayId, "Comprobante emitido", request.type_payment.name)
            applyBoldStyleToAttribute(tvMethodPayId, "Pago por", request.method_payment.name)

            when (request.state.name) {
                "Aprobado" -> {
                    tvStateId.setTextColor(
                        ContextCompat.getColor(itemView.context,
                        R.color.colorActivo
                    ))
                }
                "Rechazado" -> {
                    tvStateId.setTextColor(
                        ContextCompat.getColor(itemView.context,
                        R.color.colorInactivo
                    ))
                }
                "Por validar" -> {
                    tvStateId.setTextColor(
                        ContextCompat.getColor(itemView.context,
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
