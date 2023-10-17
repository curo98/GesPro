package iberoplast.pe.gespro.ui

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.ScrollView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import iberoplast.pe.gespro.R
import iberoplast.pe.gespro.io.ApiService
import iberoplast.pe.gespro.model.MethodPayment
import iberoplast.pe.gespro.model.StateRequest
import iberoplast.pe.gespro.model.TypePayment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterRequestSupplierActivity : AppCompatActivity() {

    private var currentStep = 1 // Valor predeterminado: paso 1 (20%)
    private lateinit var progressBar: ProgressBar

    private val apiService: ApiService by lazy {
        ApiService.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_request_supplier)


        progressBar = findViewById(R.id.progressBar)

        // Variables
        val btnNextFormRequest1 = findViewById<Button>(R.id.btnNextFormRequest1)
        val btnNextFormRequest2 = findViewById<Button>(R.id.btnNextFormRequest2)
        val btnNextFormRequest3 = findViewById<Button>(R.id.btnNextFormRequest3)
        val btnNextFormRequest4 = findViewById<Button>(R.id.btnNextFormRequest4)

        val llFormStep1 = findViewById<LinearLayout>(R.id.llFormStep1)
        val llFormStep2 = findViewById<LinearLayout>(R.id.llFormStep2)
        val llFormStep3 = findViewById<LinearLayout>(R.id.llFormStep3)
        val llFormStep4 = findViewById<LinearLayout>(R.id.llFormStep4)
        val llFormStep5 = findViewById<LinearLayout>(R.id.llFormStep5)

        // Start code formstep1
        btnNextFormRequest1.setOnClickListener{
            val etNicRuc = findViewById<EditText>(R.id.etNicRuc)
            val nicRucText = etNicRuc.text.toString().trim()

            if (nicRucText.isEmpty()) {
                etNicRuc.error = "El NIC o RUC no puede estar vacío"
            } else if (!nicRucText.matches(Regex("^[0-9]+$"))) {
                etNicRuc.error = "Solo se permiten números en el NIC o RUC"
            } else if (nicRucText.length < 10) {
                etNicRuc.error = "El NIC o RUC es demasiado corto"
            } else {
                // Continuar con el paso 2(TEST)
                llFormStep1.visibility = View.GONE
                llFormStep2.visibility = View.VISIBLE
//                val intent = Intent(this, RequestSupplierStep2Activity::class.java)
//                startActivity(intent)

                currentStep = 2
                // Actualizar la barra de progreso
                updateProgressBar()
            }
        }
        // end code formstep1

        // Start code formstep2
        val spPaises = findViewById<Spinner>(R.id.spinnerPaises)
        val spDepartamentos = findViewById<Spinner>(R.id.spinnerDepartamentos)
        val spDistritos = findViewById<Spinner>(R.id.spinnerDistritos)

        val paises = arrayOf("Peru", "Colombia", "Chile", "Venezuela")
        val departamentos = arrayOf("Lima", "Iquitos", "Ica", "Puno")
        val distritos = arrayOf("San vicente de canete", "La victoria", "Pucusana", "Juliaca")


        spPaises.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, paises)
        spDepartamentos.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, departamentos)
        spDistritos.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, distritos)

        btnNextFormRequest2.setOnClickListener{
            val etNomProveedor = findViewById<EditText>(R.id.etNomProveedor)
            val etCorreo = findViewById<EditText>(R.id.etCorreo)
            val etDir1 = findViewById<EditText>(R.id.etDir1)
            val etDir2 = findViewById<EditText>(R.id.etDir2)

            val nombreProveedor = etNomProveedor.text.toString()
            val correo = etCorreo.text.toString()
            val dir1 = etDir1.text.toString()
            val dir2 = etDir2.text.toString()

//            val rgCondPago = findViewById<RadioGroup>(R.id.rgCondPago)
//            val selCondPagoId = rgCondPago.checkedRadioButtonId
//            val selTipoCon = rgCondPago.findViewById<RadioButton>(selCondPagoId)
//            val tipoConPago = selTipoCon.text .toString()
//
//            val rgTipoPago = findViewById<RadioGroup>(R.id.rgTipoPago)
//            val selTipoPagoId = rgTipoPago.checkedRadioButtonId
//            val selTipoPago = rgTipoPago.findViewById<RadioButton>(selTipoPagoId)
//            val tipoPago = selTipoPago.text .toString()

            if (nombreProveedor.isEmpty() || dir1.isEmpty() || dir2.isEmpty()) {
                // Manejar la validación de campos vacíos aquí
                etNomProveedor.error = "Este campo no puede estar vacío"
                etDir1.error = "Este campo no puede estar vacío"
                etDir2.error = "Este campo no puede estar vacío"
            }
            // Validación del formato de correo electrónico
            else if (correo.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                etCorreo.error = "Ingresa un correo electrónico válido"
            }
            else{
                // Continuar con el paso 2(TEST)
                llFormStep2.visibility = View.GONE
                llFormStep3.visibility = View.VISIBLE


                currentStep = 3
                // Actualizar la barra de progreso
                updateProgressBar()
            }



        }
        // end code formstep2

        // Start code formstep3
        val webView = findViewById<WebView>(R.id.webView)

        val url = "https://drive.google.com/file/d/1Y4gwaZnYgfpS2Y2eE6CtMEnMMapuA5aA/view?usp=drive_link"
        // Reemplaza "ENLACE_PDF" con el enlace de Google Drive a tu archivo PDF

        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        webView.loadUrl(url)

        btnNextFormRequest3.setOnClickListener{
            val checkBox1 = findViewById<CheckBox>(R.id.checkBox1)
            val checkBox2 = findViewById<CheckBox>(R.id.checkBox2)

            val aceptoPoliticaPro = checkBox1.isChecked
            val aceptoPoliticaDatos = checkBox2.isChecked

            if (!aceptoPoliticaPro && !aceptoPoliticaDatos) {
                // Manejar la validación de que checkBox1 no está marcado
                val layout = findViewById<ScrollView>(R.id.layout) // Reemplaza "layout" con el ID de tu diseño
                val snackbar = Snackbar.make(layout, "Debe aceptar nuestras politicas", Snackbar.LENGTH_LONG)
                snackbar.show()
            }
            else if (!aceptoPoliticaPro) {
                // Manejar la validación de que checkBox1 no está marcado
                val layout = findViewById<ScrollView>(R.id.layout) // Reemplaza "layout" con el ID de tu diseño
                val snackbar = Snackbar.make(layout, "Debe aceptar nuestra politica de proveedores", Snackbar.LENGTH_LONG)
                snackbar.show()
            }

            else if (!aceptoPoliticaDatos) {
                // Manejar la validación de que checkBox1 no está marcado
                val layout = findViewById<ScrollView>(R.id.layout) // Reemplaza "layout" con el ID de tu diseño
                val snackbar = Snackbar.make(layout, "Debe aceptar nuestra politica de proteccion de datos", Snackbar.LENGTH_LONG)
                snackbar.show()
            }
            else {
                // Ambos CheckBox están marcados, puedes continuar con tus acciones
                // Continuar con el paso 3(TEST)
                llFormStep3.visibility = View.GONE
                llFormStep4.visibility = View.VISIBLE

                currentStep = 4
                // Actualizar la barra de progreso
                updateProgressBar()
            }
        }
        // end code formstep3

        // Start code formstep4
        btnNextFormRequest4.setOnClickListener{
            // Continuar con el paso 4(TEST)
            llFormStep4.visibility = View.GONE
            llFormStep5.visibility = View.VISIBLE

            currentStep = 5
            // Actualizar la barra de progreso
            updateProgressBar()
        }
        // end code formstep4


        // Start code formstep5
        val btnSend = findViewById<Button>(R.id.btnSend)

        btnSend.setOnClickListener {
            // Mostrar un mensaje Toast
            Toast.makeText(this, "Solicitud de registro proveedor enviada", Toast.LENGTH_SHORT).show()

            // Redirigir a MenuActivity
//            val intent = Intent(this, MenuActivity::class.java)
//            startActivity(intent)

            // Finalizar la actividad actual
            finish()
        }
        // end code formstep5

        loadStatesRequest()
        loadTypesPayments()
        loadMethodsPayments()
    }

    private fun loadStatesRequest() {
        val spEstados = findViewById<Spinner>(R.id.spinnerEstados)

        val call = apiService.getStates()
        call.enqueue(object: Callback<ArrayList<StateRequest>> {

            override fun onFailure(call: Call<ArrayList<StateRequest>>, t: Throwable) {
                Toast.makeText(this@RegisterRequestSupplierActivity, "Ocurrio un problema al cargar los estados del formulario", Toast.LENGTH_SHORT).show()
                finish()
            }

            override fun onResponse(
                call: Call<ArrayList<StateRequest>>,
                response: Response<ArrayList<StateRequest>>
            ) {
                if (response.isSuccessful){
                    val states = response.body()

                    val dataStateRequest = ArrayList<String>()
                    states?.forEach {
                        dataStateRequest.add(it.name)
                    }


                    spEstados.adapter = ArrayAdapter(this@RegisterRequestSupplierActivity, android.R.layout.simple_list_item_1, dataStateRequest)
                }
            }

        })

    }

    private fun loadTypesPayments() {
        val rgTypesPayments = findViewById<RadioGroup>(R.id.rgCondPago) // Referencia al RadioGroup

        val call = apiService.getTypesPayments()
        call.enqueue(object: Callback<ArrayList<TypePayment>> {

            override fun onFailure(call: Call<ArrayList<TypePayment>>, t: Throwable) {
                Toast.makeText(this@RegisterRequestSupplierActivity, "Ocurrió un problema al cargar los tipos de pago del formulario", Toast.LENGTH_SHORT).show()
                finish()
            }

            override fun onResponse(
                call: Call<ArrayList<TypePayment>>,
                response: Response<ArrayList<TypePayment>>
            ) {
                if (response.isSuccessful) {
                    val typesPayments = response.body()

                    // Verifica si se recibieron datos
                    if (typesPayments != null) {
                        // Recorre los datos y agrega opciones al RadioGroup
                        for (typePayment in typesPayments) {
                            val radioButton = RadioButton(this@RegisterRequestSupplierActivity)
                            radioButton.text = typePayment.name
                            radioButton.id = View.generateViewId() // Asigna un ID único
                            // Obtiene el valor del padding desde dimens.xml
                            val paddingInPx = resources.getDimension(R.dimen.radio_button_padding).toInt()

                            // Aplica el padding a los RadioButtons
                            radioButton.setPadding(paddingInPx, paddingInPx, paddingInPx, paddingInPx)

                            val params = RadioGroup.LayoutParams(
                                RadioGroup.LayoutParams.WRAP_CONTENT,
                                RadioGroup.LayoutParams.WRAP_CONTENT
                            )
                            params.weight = 1f
                            params.width = 0
                            radioButton.layoutParams = params
                            rgTypesPayments.addView(radioButton)
                        }

                        // Establece un listener para gestionar la selección
                        rgTypesPayments.setOnCheckedChangeListener { group, checkedId ->
                            val selectedRadioButton = findViewById<RadioButton>(checkedId)
                            val selectedTypePayment = selectedRadioButton.text.toString()
                            // Hacer algo con el tipo de pago seleccionado
                        }
                    }
                }
            }
        })
    }

    private fun loadMethodsPayments() {
        val rgMethodsPayments = findViewById<RadioGroup>(R.id.rgTipoPago) // Referencia al RadioGroup

        val call = apiService.getMethodsPayments()
        call.enqueue(object: Callback<ArrayList<MethodPayment>> {

            override fun onFailure(call: Call<ArrayList<MethodPayment>>, t: Throwable) {
                Toast.makeText(this@RegisterRequestSupplierActivity, "Ocurrió un problema al cargar los tipos de pago del formulario", Toast.LENGTH_SHORT).show()
                finish()
            }

            override fun onResponse(
                call: Call<ArrayList<MethodPayment>>,
                response: Response<ArrayList<MethodPayment>>
            ) {
                if (response.isSuccessful) {
                    val methodsPayments = response.body()

                    // Verifica si se recibieron datos
                    if (methodsPayments != null) {
                        // Recorre los datos y agrega opciones al RadioGroup
                        for (typePayment in methodsPayments) {
                            val radioButton = RadioButton(this@RegisterRequestSupplierActivity)
                            radioButton.text = typePayment.name
                            radioButton.id = View.generateViewId() // Asigna un ID único
                            // Obtiene el valor del padding desde dimens.xml
                            val paddingInPx = resources.getDimension(R.dimen.radio_button_padding).toInt()

                            // Aplica el padding a los RadioButtons
                            radioButton.setPadding(paddingInPx, paddingInPx, paddingInPx, paddingInPx)

                            val params = RadioGroup.LayoutParams(
                                RadioGroup.LayoutParams.WRAP_CONTENT,
                                RadioGroup.LayoutParams.WRAP_CONTENT
                            )
                            params.weight = 1f
                            params.width = 0
                            radioButton.layoutParams = params
                            rgMethodsPayments.addView(radioButton)
                        }

                        // Establece un listener para gestionar la selección
                        rgMethodsPayments.setOnCheckedChangeListener { group, checkedId ->
                            val selectedRadioButton = findViewById<RadioButton>(checkedId)
                            val selectedMethodPayment = selectedRadioButton.text.toString()
                            // Hacer algo con el tipo de pago seleccionado
                        }
                    }
                }
            }
        })
    }

    override fun onBackPressed() {
        val llFormStep1 = findViewById<LinearLayout>(R.id.llFormStep1)
        val llFormStep2 = findViewById<LinearLayout>(R.id.llFormStep2)
        val llFormStep3 = findViewById<LinearLayout>(R.id.llFormStep3)
        val llFormStep4 = findViewById<LinearLayout>(R.id.llFormStep4)
        val llFormStep5 = findViewById<LinearLayout>(R.id.llFormStep5)

        if (llFormStep5.visibility == View.VISIBLE && currentStep == 5) {
            llFormStep5.visibility = View.GONE
            llFormStep4.visibility = View.VISIBLE

            currentStep = 4 // Actualiza el paso actual a 4
            updateProgressBar() // Actualiza la barra de progreso
        }
        else if (llFormStep4.visibility == View.VISIBLE && currentStep == 4) {
            llFormStep4.visibility = View.GONE
            llFormStep3.visibility = View.VISIBLE

            currentStep = 3 // Actualiza el paso actual a 3
            updateProgressBar() // Actualiza la barra de progreso
        }
        else if (llFormStep3.visibility == View.VISIBLE && currentStep == 3) {
            llFormStep3.visibility = View.GONE
            llFormStep2.visibility = View.VISIBLE

            currentStep = 2 // Actualiza el paso actual a 2
            updateProgressBar() // Actualiza la barra de progreso
        }
        else if (llFormStep2.visibility == View.VISIBLE && currentStep == 2) {
            llFormStep2.visibility = View.GONE
            llFormStep1.visibility = View.VISIBLE

            currentStep = 1 // Actualiza el paso actual a 1
            updateProgressBar() // Actualiza la barra de progreso
        }
        else if (llFormStep1.visibility == View.VISIBLE) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Estas seguro que quieres salir?")
            builder.setMessage("Si abandonas el registro, los datos que ingresastes se perderan")
            builder.setPositiveButton("Si, salir") { _, _ ->
                finish()
            }

            builder.setNegativeButton("Continuar con el registro"){ dialog, _ ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }
    }

    private fun updateProgressBar() {
        val progress = when (currentStep) {
            1 -> 20
            2 -> 40
            3 -> 60
            4 -> 80
            5 -> 100
            else -> 0
        }

        progressBar.progress = progress
    }
}