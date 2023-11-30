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
import iberoplast.pe.gespro.model.SupplierRequest

class SupplierRequestAdapter : RecyclerView.Adapter<SupplierRequestAdapter.ViewHolder>() {

    private var requests = ArrayList<SupplierRequest>()
    private var filteredList = ArrayList<SupplierRequest>()
    var selectedRequest: SupplierRequest? = null

    fun updateRequests(newRequests: List<SupplierRequest>) {
        requests.clear()
        requests.addAll(newRequests)
        filterRequests("")
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterRequests(query: String) {
        filteredList.clear()

        if (query.isEmpty()) {
            filteredList.addAll(requests)
        } else {
            for (request in requests) {
                if (request.id.toString().toLowerCase().contains(query)) {
                    filteredList.add(request)
                }
            }
        }

        notifyDataSetChanged()
    }

    // Renombrar la función que utiliza el operador de asignación personalizado
    fun setNewSelectedRequest(request: SupplierRequest?) {
        selectedRequest = request
    }

    fun getPosition(request: SupplierRequest?): Int {
        request?.let {
            for (index in 0 until requests.size) {
                if (requests[index] == request) {
                    return index
                }
            }
        }
        return -1 // Usuario no encontrado en la lista
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
                    "Aprobada" -> {
                        tvStateId.text = finalState.name
                        tvStateId.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.colorApproved
                            )
                        )
                    }

                    "Desaprobada" -> {
                        tvStateId.text = finalState.name
                        tvStateId.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.colorInactivo
                            )
                        )
                    }

                    "Por validar" -> {
                        tvStateId.text = finalState.name
                        tvStateId.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.colorPendiente
                            )
                        )
                    }
                    "Validada" -> {
                        tvStateId.text = finalState.name
                        tvStateId.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.colorValidated
                            )
                        )
                    }
                    "Por aprobar" -> {
                        tvStateId.text = finalState.name
                        tvStateId.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.colorToApprove
                            )
                        )
                    }
                    "Por recibir" -> {
                        tvStateId.text = finalState.name
                        tvStateId.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.colorToReceive
                            )
                        )
                    }
                    "Recibida" -> {
                        tvStateId.text = finalState.name
                        tvStateId.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.colorReceived
                            )
                        )
                    }
                    "Enviado" -> {
                        tvStateId.text = finalState.name
                        tvStateId.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.colorActivo
                            )
                        )
                    }
                    "Cancelada" -> {
                        tvStateId.text = finalState.name
                        tvStateId.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.colorCanceled
                            )
                        )
                    }
                    else -> {
                        tvStateId.text = finalState.name
                        tvStateId.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.colorPrimaryDark
                            )
                        ) // Establece un color predeterminado si no coincide con los casos anteriores
                    }
                }
            } else {
                // No hay transiciones de estado, maneja esto como desees
                tvStateId.text = "Sin transiciones"
                tvStateId.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.colorPrimaryDark
                    )
                ) // Establece un color predeterminado si no hay transiciones
            }

            applyBoldStyleToAttribute(tvTypePayId, "Comprobante emitido", request.type_payment.name)
            applyBoldStyleToAttribute(tvMethodPayId, "Pago por", request.method_payment.name)
        }

        private fun applyBoldStyleToAttribute(textView: TextView, attribute: String, value: Any?) {

            val text = if (
                    value == "Aprobada" || value == "Desaprobada" || value == "Enviado"
                    || value == "Recibida" || value == "Por validar"
                    || value == "Por aprobar" || value == "Por recibir"
                    || value == "Cancelada" || value == "Por validar" || value == "Validada"
                )
            {
                "$value"
            } else {
                "$attribute: $value"
            }

            val spannableString = SpannableString(text)
            spannableString.setSpan(
                StyleSpan(Typeface.BOLD),
                0,
                attribute.length,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            textView.text = spannableString
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_supplier_request, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val request = filteredList[position]

        holder.bind(request)
        // Agregar OnLongClickListener a la vista raíz del elemento
        holder.itemView.setOnLongClickListener { view ->
            setNewSelectedRequest(request)
            // Muestra el menú contextual
            view.showContextMenu()
            true
        }
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

}