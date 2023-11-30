package iberoplast.pe.gespro.ui

import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import iberoplast.pe.gespro.R
import iberoplast.pe.gespro.io.ApiService
import iberoplast.pe.gespro.io.response.charts.ChartDataResponse
import iberoplast.pe.gespro.io.response.charts.ResponseCounts
import iberoplast.pe.gespro.io.response.charts.UserRoleResponse
import iberoplast.pe.gespro.ui.auth.LoginActivity
import iberoplast.pe.gespro.ui.modules.condition_payment.ConditionPaymentActivity
import iberoplast.pe.gespro.ui.modules.method_payment.MethodPaymentActivity
import iberoplast.pe.gespro.ui.modules.question.QuestionActivity
import iberoplast.pe.gespro.ui.modules.request.FormRequestSupplierActivity
import iberoplast.pe.gespro.ui.modules.request.SupplierRequestsActivity
import iberoplast.pe.gespro.ui.modules.role.RoleActivity
import iberoplast.pe.gespro.ui.modules.state.StateRequestActivity
import iberoplast.pe.gespro.ui.modules.supplier.FormSupplierActivity
import iberoplast.pe.gespro.ui.modules.supplier.SuppliersActivity
import iberoplast.pe.gespro.ui.modules.user.UserActivity
import iberoplast.pe.gespro.util.ActionBarUtils
import iberoplast.pe.gespro.util.PreferenceHelper
import iberoplast.pe.gespro.util.PreferenceHelper.get
import iberoplast.pe.gespro.util.PreferenceHelper.set
import iberoplast.pe.gespro.util.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Random
import kotlin.math.roundToInt

class MenuActivity : AppCompatActivity() {
    private val apiService by lazy {
        ApiService.create()
    }

    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this@MenuActivity)
    }
    private lateinit var barChart: BarChart
    private lateinit var barChartTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val userRoleName = preferences["UserRolePreferences", ""]

        if (userRoleName == "admin") {
            ActionBarUtils.setCustomTitle(
                this,
                "Panel de control",
                "Administrador"
            )
        } else {
            ActionBarUtils.setCustomTitle(
                this,
                "menu principal",
                ""
            )
        }

        Log.d("UserRoleMenuActivity", "Rol del usuario: $userRoleName")
        if (userRoleName == "admin"){
            val llMenu = findViewById<LinearLayout>(R.id.llMenu)
            val llCharts = findViewById<LinearLayout>(R.id.llCharts)
            llMenu.visibility = View.GONE
            llCharts.visibility = View.VISIBLE

            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val response = apiService.getUsersByRole().execute()
                    if (response.isSuccessful) {
                        val userRoleResponse = response.body()
                        withContext(Dispatchers.Main) {
                            updatePieChart(userRoleResponse)
                        }
                    }
                } catch (e: Exception) {}
            }

            targetsCounts()
            barChart()

        }

        val storeToken = intent.getBooleanExtra("store_token", false)
        if (storeToken){
            storeToken()
        }
        val btnCrearSolicitud = findViewById<Button>(R.id.btnCrearSolicitud)
        val btnListProveedores = findViewById<Button>(R.id.btn_list_proveedores)
        val btnListSolicitudes = findViewById<Button>(R.id.btn_list_solicitudes)
        val btnProfile = findViewById<Button>(R.id.btnProfile)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        btnCrearSolicitud.setOnClickListener{
            val intent = Intent(this, FormRequestSupplierActivity::class.java)
            startActivity(intent)
        }

        btnListProveedores.setOnClickListener{
            val intent = Intent(this, SuppliersActivity::class.java)
            startActivity(intent)
        }

        btnListSolicitudes.setOnClickListener{
            val intent = Intent(this, SupplierRequestsActivity::class.java)
            startActivity(intent)
        }

        btnLogout.setOnClickListener{
            performLogout()
        }

        when (userRoleName) {
            "admin" -> {
                // Rol admin: Mostrar o habilitar los botones necesarios
                btnProfile.visibility = View.VISIBLE
                btnCrearSolicitud.visibility = View.GONE
                btnListProveedores.visibility = View.VISIBLE
                btnListSolicitudes.visibility = View.VISIBLE
            }
            "compras" -> {
                // Rol compras: Mostrar o habilitar los botones necesarios
                btnProfile.visibility = View.VISIBLE
                btnCrearSolicitud.visibility = View.GONE
                btnListProveedores.visibility = View.VISIBLE
                btnListSolicitudes.visibility = View.VISIBLE
            }
            "contabilidad" -> {
                // Rol contabilidad: Mostrar o habilitar los botones necesarios
                btnProfile.visibility = View.VISIBLE
                btnCrearSolicitud.visibility = View.GONE
                btnListProveedores.visibility = View.VISIBLE
                btnListSolicitudes.visibility = View.VISIBLE
            }
            "proveedor" -> {
                // Rol proveedor: Mostrar u ocultar los botones necesarios
                btnProfile.visibility = View.VISIBLE
                btnCrearSolicitud.visibility = View.VISIBLE
                btnListProveedores.visibility = View.GONE
                btnListSolicitudes.visibility = View.VISIBLE
            }
            // Agrega más casos según tus roles
            else -> {
                // Otros roles: Mostrar u ocultar los botones según sea necesario
                // Puedes establecer la visibilidad a GONE para ocultarlos completamente
                btnProfile.visibility = View.VISIBLE
                btnCrearSolicitud.visibility = View.VISIBLE
                btnListProveedores.visibility = View.GONE
                btnListSolicitudes.visibility = View.GONE
            }
        }

    }

    private fun barChart() {
        barChart = findViewById(R.id.barChart)
        barChartTitle = findViewById(R.id.barChartTitle)
        val call = apiService.requestsByWeek()
        call.enqueue(object : Callback<ChartDataResponse> {
            override fun onResponse(
                call: Call<ChartDataResponse>,
                response: Response<ChartDataResponse>
            ) {
                if (response.isSuccessful) {
                    val chartData = response.body()
                    if (chartData != null) {
                        setupBarChart(barChart, chartData, barChartTitle)
                    }
                }
            }

            override fun onFailure(call: Call<ChartDataResponse>, t: Throwable) {
            }
        })
    }

    fun setupBarChart(barChart: BarChart, chartData: ChartDataResponse, barChartTitle: TextView) {
        val barEntries = ArrayList<BarEntry>()

        for (i in chartData.data.indices) {
            // Redondear los valores a enteros
            val value = chartData.data[i].toFloat().roundToInt()
            barEntries.add(BarEntry(i.toFloat(), value.toFloat()))
        }

        val barDataSet = BarDataSet(barEntries, "Solicitudes por Mes")

        // Colores aleatorios
        val colors = ArrayList<Int>()
        for (i in 0 until chartData.labels.size) {
            colors.add(generateRandomColor())
        }
        barDataSet.colors = colors

        val barData = BarData(barDataSet)

        // Personalizar el aspecto del BarChart
        barChart.data = barData
        barChart.setFitBars(true)
        barChart.description.isEnabled = false
        barChart.legend.isEnabled = false

        val xAxis = barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(chartData.labels.toTypedArray())
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f

        // Agregar título al gráfico utilizando un TextView
        barChartTitle.text = "Número de Solicitudes por Mes"
        barChartTitle.textSize = 16f
        barChartTitle.setTextColor(Color.BLACK)

        barChart.invalidate()
    }

    private fun targetsCounts() {
        // Referencia al TextView en tu diseño
        val tvCountRequest = findViewById<TextView>(R.id.tvCountRequest)
        val tvCountUsers = findViewById<TextView>(R.id.tvCountUsers)

        val call = apiService.getCounts()
        call.enqueue(object : Callback<ResponseCounts> {
            override fun onResponse(
                call: Call<ResponseCounts>,
                response: Response<ResponseCounts>
            ) {
                if (response.isSuccessful) {
                    val counts: ResponseCounts? = response.body()
                    counts?.let {
                        // Actualiza tu interfaz de usuario con los recuentos
                        tvCountRequest.text = it.countRequests.toString()
                        tvCountUsers.text = it.countUsers.toString()

                        animateTextView(tvCountRequest)
                        animateTextView(tvCountUsers)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseCounts>, t: Throwable) {
                t.localizedMessage?.let { toast(it) }
            }

        })
    }
    private fun animateTextView(textView: TextView) {
        // Obtener el valor actual y el valor final del TextView
        val currentValue = 0
        val finalValue = textView.text.toString().toInt()

        // Crear un ValueAnimator
        val animator = ValueAnimator.ofInt(currentValue, finalValue)

        // Añadir un actualizador para modificar el valor del TextView durante la animación
        animator.addUpdateListener { animation ->
            val animatedValue = animation.animatedValue as Int
            textView.text = animatedValue.toString()
        }

        // Establecer la duración de la animación
        animator.duration = 1000 // Duración en milisegundos

        // Iniciar la animación
        animator.start()
    }


    private fun updatePieChart(userRoleResponse: List<UserRoleResponse>?) {
        userRoleResponse?.let { roles ->
            val pieChart = findViewById<PieChart>(R.id.pieChart)

            val total = roles.sumBy { it.count }

            val pieEntries = roles.map { role ->
                val percentage = (role.count.toFloat() / total) * 100
                PieEntry(percentage, role.role, role.count)
            }

            val randomColors = List(pieEntries.size) { generateRandomColor() }

            val pieDataSet = PieDataSet(pieEntries, "").apply {
                colors = randomColors
                sliceSpace = 1f // Ajustar según sea necesario para la separación entre las porciones
                valueTextSize = 11f // Ajustar según sea necesario para el tamaño del texto
            }

            val pieData = PieData(pieDataSet).apply {
                setValueFormatter(object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return "${value.toInt()}%" // Agregar el signo de porcentaje
                    }
                })
            }

            pieChart.apply {
                data = pieData
                setDrawCenterText(true)
                centerText = "Roles"
                setExtraOffsets(10f, 10f, 10f, 10f) // Ajustar según sea necesario para los desplazamientos adicionales

                // Configuración de la leyenda para colocarla debajo del gráfico y centrada
                legend.apply {
                    verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                    horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                    orientation = Legend.LegendOrientation.HORIZONTAL
                    setDrawInside(true)
                }
                // Ajustar el tamaño del PieChart dinámicamente
                layoutParams.width = resources.displayMetrics.widthPixels
                layoutParams.height = resources.displayMetrics.widthPixels // Puedes ajustar según tus necesidades

                invalidate()

                setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                    override fun onValueSelected(e: Entry?, h: Highlight?) {
                        Toast.makeText(
                            applicationContext,
                            "Usuarios en este rol: ${e?.data as Int}", // Mostrar el valor original al hacer clic
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onNothingSelected() {
                        // No hacer nada cuando no se selecciona nada
                    }
                })
            }
        }
    }



    private fun generateRandomColor(): Int {
        val random = Random()
        return Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256))
    }



    fun editProfile(view: View){
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }

    private fun storeToken()
    {
        val jwt = preferences["jwt", ""]
        val authHeader = "Bearer $jwt"
        Firebase.messaging.token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                println("el token no fue generado")
                return@addOnCompleteListener
            }
            // Get new FCM registration token
            val deviceToken = task.result

            val call = apiService.postToken(authHeader, deviceToken)
            call.enqueue(object: Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful){
                        Log.d(TAG, "Token registrado correctamente")
                    }else{
                        Log.d(TAG, "Ocurrio un error al registrar el token")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    t.localizedMessage?.let { toast(it) }
                }

            })
        }

    }

    /* START MENU */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_in_activity, menu)

        // Accede al rol del usuario desde tus preferencias
        val preferences = PreferenceHelper.defaultPrefs(this)
        val userRoleName = preferences["UserRolePreferences", ""]

        // Accede a los elementos de menú que deseas mostrar u ocultar según el rol del usuario
        val imAdministration = menu?.findItem(R.id.imAdministration)
        val imSuppliers = menu?.findItem(R.id.imSuppliers)
        val imRequests = menu?.findItem(R.id.imRequests)
        val imCreateRequest = menu?.findItem(R.id.imCreateRequest)
        val imListMyRequests = menu?.findItem(R.id.imListMyRequests)
        val imcreateSupplier = menu?.findItem(R.id.imCreateSupplier)

        when (userRoleName) {
            "admin" -> {
                imAdministration?.isVisible = true
                imSuppliers?.isVisible = true
                imRequests?.isVisible = true
                imCreateRequest?.isVisible = false
                imListMyRequests?.isVisible = false
            }
            "compras" -> {
                imAdministration?.isVisible = false
                imSuppliers?.isVisible = true
                imRequests?.isVisible = true
                imCreateRequest?.isVisible = false
                imListMyRequests?.isVisible = false
                imcreateSupplier?.isVisible = false
            }
            "contabilidad" -> {
                imAdministration?.isVisible = false
                imSuppliers?.isVisible = true
                imRequests?.isVisible = true
                imCreateRequest?.isVisible = false
                imListMyRequests?.isVisible = false
                imcreateSupplier?.isVisible = false
            }
            "proveedor" -> {
                imAdministration?.isVisible = false
                imSuppliers?.isVisible = false
                imRequests?.isVisible = false
                imCreateRequest?.isVisible = true
                imListMyRequests?.isVisible = true
            }
            else -> {
                imAdministration?.isVisible = false
                imSuppliers?.isVisible = false
                imRequests?.isVisible = false
                imCreateRequest?.isVisible = true
                imListMyRequests?.isVisible = false
            }
        }

        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent: Intent = when (item.itemId) {
            R.id.imMyProfile -> Intent(this, ProfileActivity::class.java)
            R.id.imStates -> Intent(this, StateRequestActivity::class.java)
            R.id.imTypePayments -> Intent(this, ConditionPaymentActivity::class.java)
            R.id.imMethodPayments -> Intent(this, MethodPaymentActivity::class.java)
            R.id.imQuestions -> Intent(this, QuestionActivity::class.java)
            R.id.imRoles -> Intent(this, RoleActivity::class.java)
            R.id.imUsers -> Intent(this, UserActivity::class.java)
            R.id.imCreateSupplier -> Intent(this, FormSupplierActivity::class.java)
            R.id.imListSuppliers -> Intent(this, SuppliersActivity::class.java)
            R.id.imCreateRequest -> Intent(this, FormRequestSupplierActivity::class.java)
            R.id.imListMyRequests, R.id.imListRequests -> Intent(this, SupplierRequestsActivity::class.java)
            R.id.imLogout -> {
                performLogout()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }

        startActivity(intent)
        return true
    }
    private fun performLogout(){
        val jwt = preferences["jwt", ""]
        val call = apiService.postLogout("Bearer $jwt")
        call.enqueue(object: Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                clearSessionPreference()
                goToLogin()
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                t.localizedMessage?.let { toast(it) }
            }

        })
    }
    /* END MENU CODE */

    private fun clearSessionPreference() {
        preferences["jwt"] = ""
        preferences["UserRolePreferences"] = ""
        preferences["UserName"] = ""
    }
    private fun goToLogin(){
        val intent = Intent(this@MenuActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
    companion object {
        private const val TAG = "MenuActivity"
    }
}
