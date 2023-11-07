package iberoplast.pe.gespro.ui.modules.requests

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import iberoplast.pe.gespro.R
import iberoplast.pe.gespro.model.Supplier
import iberoplast.pe.gespro.ui.modules.supplier.SuppliersActivity
import java.text.SimpleDateFormat
import java.util.Locale

class ShowSupplierActivity : AppCompatActivity() {
    // Definir las variables val para los TextViews
    private val tvName by lazy { findViewById<TextView>(R.id.tvName) }
    private val tvNicRuc by lazy { findViewById<TextView>(R.id.tvNicRuc) }
    private val tvCountry by lazy { findViewById<TextView>(R.id.tvCountry) }
    private val tvEmail by lazy { findViewById<TextView>(R.id.tvEmail) }
    private val tvPhone by lazy { findViewById<TextView>(R.id.tvPhone) }
    private val tvState by lazy { findViewById<TextView>(R.id.tvState) }
    private val tvCreated_at by lazy { findViewById<TextView>(R.id.tvCreated_at) }
    private val tvUpdated_at by lazy { findViewById<TextView>(R.id.tvUpdated_at) }
    private val btnReturnList by lazy { findViewById<TextView>(R.id.btnReturnList) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_supplier)

        // Recuperar los datos del proveedor del Intent
        val supplier = intent.getParcelableExtra<Supplier>("supplier_details")

        // Verificar si los datos del proveedor no son nulos
        if (supplier != null) {
            val nacionality = supplier.nacionality
            val nicRuc = supplier.nic_ruc
            val state = supplier.state

            // Formatear las fechas en formato "DD/MM/YY"
            val created_at = formatDate(supplier.created_at)
            val updated_at = formatDate(supplier.updated_at)

            // Mostrar los valores en los TextViews
            tvCountry.text = nacionality
            tvNicRuc.text = nicRuc
            tvState.text = state
            if (state.equals("inactivo", ignoreCase = true)) {
                tvState.text = "Inactivo"
                tvState.setBackgroundResource(R.drawable.red_badge_background)
            } else {
                tvState.text = "Activo"
                tvState.setBackgroundResource(R.drawable.green_badge_background)
            }
            tvCreated_at.text = created_at
            tvUpdated_at.text = updated_at

            // Para mostrar los valores del usuario anidado
            val user = supplier.user
            if (user != null) {
                val userName = user.name
                val userEmail = user.email

                tvName.text = userName
                tvEmail.text = userEmail
            }
        }

        btnReturnList.setOnClickListener {
            val intent = Intent(this, SuppliersActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Función para formatear una fecha en formato "DD/MM/YY"
    private fun formatDate(dateTime: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yy", Locale.getDefault())

        try {
            val date = inputFormat.parse(dateTime)
            return outputFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            return dateTime // Si ocurre un error, devolvemos el valor original
        }
    }
    // Función para aplicar un "badge" rojo
    private fun applyRedBadge() {
        tvState.setBackgroundResource(R.drawable.red_badge_background) // Define el fondo rojo
        tvState.setTextColor(resources.getColor(R.color.red)) // Define el color de texto
    }

    // Función para aplicar un "badge" verde
    private fun applyGreenBadge() {
        tvState.setBackgroundResource(R.drawable.green_badge_background) // Define el fondo verde
        tvState.setTextColor(resources.getColor(R.color.green)) // Define el color de texto
    }
}