package iberoplast.pe.gespro.ui.modules.supplier

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import iberoplast.pe.gespro.R
import iberoplast.pe.gespro.model.Supplier
import iberoplast.pe.gespro.util.ActionBarUtils
import java.text.SimpleDateFormat
import java.util.Locale

class ShowSupplierActivity : AppCompatActivity() {
    // Definir las variables val para los TextViews
    private val tvName by lazy { findViewById<TextView>(R.id.tvName) }
    private val tvNicRuc by lazy { findViewById<TextView>(R.id.tvNicRuc) }
    private val tvEmail by lazy { findViewById<TextView>(R.id.tvEmail) }
    private val tvPhone by lazy { findViewById<TextView>(R.id.tvPhone) }
    private val tvState by lazy { findViewById<TextView>(R.id.tvState) }
    private val ivProfile by lazy { findViewById<ImageView>(R.id.ivProfile) }
    private val tvCreated_at by lazy { findViewById<TextView>(R.id.tvCreated_at) }
    private val tvUpdated_at by lazy { findViewById<TextView>(R.id.tvUpdated_at) }
    private val btnReturnList by lazy { findViewById<TextView>(R.id.btnReturnList) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_supplier)

        // Recuperar los datos del proveedor del Intent
        val supplier = intent.getParcelableExtra<Supplier>("supplier_details")

        val ivFlagCountry: ImageView = findViewById(R.id.ivFlagCountry)
        // Verificar si los datos del proveedor no son nulos
        if (supplier != null) {
            ActionBarUtils.setCustomTitle(
                this,
                "Detalle de proveedor:",
                "${supplier.user.name}"
            )
            val nicRuc = supplier.nic_ruc
            val state = supplier.state

            // Formatear las fechas en formato "DD/MM/YY"
            val created_at = formatDate(supplier.created_at)
            val updated_at = formatDate(supplier.updated_at)

            // Mostrar los valores en los TextViews
            val uri = supplier.flag_country
            val uriPhoto = supplier.user.photo

            // Concatenar el dominio con la URI para formar la URL completa
            val domain = "https://gespro-iberoplast.tech"
            val flagCountryURL = "$domain$uri"
            val photoProfileURL = "$domain$uriPhoto"

            Picasso.get().load("${photoProfileURL}").into(ivProfile)
            Picasso.get().load("${flagCountryURL}").into(ivFlagCountry)
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
}