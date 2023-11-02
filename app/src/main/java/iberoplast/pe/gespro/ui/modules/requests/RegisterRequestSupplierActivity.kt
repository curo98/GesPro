package iberoplast.pe.gespro.ui.modules.requests

import android.content.Intent
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
import iberoplast.pe.gespro.model.TypePayment
import iberoplast.pe.gespro.model.ubigeo_peru.Department
import iberoplast.pe.gespro.model.ubigeo_peru.District
import iberoplast.pe.gespro.ui.modules.SuccesfulRegisterActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterRequestSupplierActivity : AppCompatActivity() {

    private var currentStep = 1 // Valor predeterminado: paso 1 (20%)
    private lateinit var progressBar: ProgressBar

    private val apiService: ApiService by lazy {
        ApiService.create()
    }
// joshep chipi
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
        btnNextFormRequest2.setOnClickListener {
            if (validateForm()) {
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
            val mensaje = "Su solicitud fue enviada satisfactoriamente."
            val intent = Intent(this, SuccesfulRegisterActivity::class.java)
            intent.putExtra("mensaje", mensaje) // Configura el mensaje en la intención antes de iniciar la actividad
            startActivity(intent)

            // Finalizar la actividad actual
            finish()
        }

        // end code formstep5

        val btnPrevForm2 = findViewById<Button>(R.id.btnPrevForm2)
        val btnPrevForm3 = findViewById<Button>(R.id.btnPrevForm3)
        val btnPrevForm4 = findViewById<Button>(R.id.btnPrevForm4)
        val btnPrevForm5 = findViewById<Button>(R.id.btnPrevForm5)

        btnPrevForm2.setOnClickListener {
            if (llFormStep2.visibility == View.VISIBLE && currentStep == 2) {
                llFormStep2.visibility = View.GONE
                llFormStep1.visibility = View.VISIBLE

                currentStep = 1 // Actualiza el paso actual a 1
                updateProgressBar() // Actualiza la barra de progreso
            }
        }

        btnPrevForm3.setOnClickListener {
            if (llFormStep3.visibility == View.VISIBLE && currentStep == 3) {
                llFormStep3.visibility = View.GONE
                llFormStep2.visibility = View.VISIBLE

                currentStep = 2 // Actualiza el paso actual a 1
                updateProgressBar() // Actualiza la barra de progreso
            }
        }

        btnPrevForm4.setOnClickListener {
            if (llFormStep4.visibility == View.VISIBLE && currentStep == 4) {
                llFormStep4.visibility = View.GONE
                llFormStep3.visibility = View.VISIBLE

                currentStep = 3 // Actualiza el paso actual a 1
                updateProgressBar() // Actualiza la barra de progreso
            }
        }

        btnPrevForm5.setOnClickListener {
            if (llFormStep5.visibility == View.VISIBLE && currentStep == 5) {
                llFormStep5.visibility = View.GONE
                llFormStep4.visibility = View.VISIBLE

                currentStep = 4 // Actualiza el paso actual a 1
                updateProgressBar() // Actualiza la barra de progreso
            }
        }

        loadTypesPayments()
        loadMethodsPayments()
        loadDepartments()
        //loadProvinces()
        loadDistricts()
    }

    private fun loadDepartments() {
        val spDepartamentos = findViewById<Spinner>(R.id.spinnerDepartamentos)
        val call = apiService.getDepartments()

        call.enqueue(object : Callback<ArrayList<Department>> {
            override fun onResponse(
                call: Call<ArrayList<Department>>,
                response: Response<ArrayList<Department>>
            ) {
                if (response.isSuccessful) {
                    val departments = response.body()

                    val departmentOptions = ArrayList<String>()
                    departments?.forEach {
                        departmentOptions.add(it.name)
                    }
                    spDepartamentos.adapter = ArrayAdapter(
                        this@RegisterRequestSupplierActivity,
                        android.R.layout.simple_list_item_1,
                        departmentOptions
                    )
                }
            }

            override fun onFailure(call: Call<ArrayList<Department>>, t: Throwable) {
                Toast.makeText(this@RegisterRequestSupplierActivity, "Ocurrió un problema", Toast.LENGTH_SHORT).show()
                finish()
            }
        })
    }

//    private fun loadProvinces() {
//        val spDepartments = findViewById<Spinner>(R.id.spinnerDepartamentos)
//        spDepartments.onItemSelectedListener = object:
//            AdapterView.OnItemSelectedListener{
//            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                val option: String = p0?.getItemAtPosition(p2) as String
//                Toast.makeText(this@RegisterRequestSupplierActivity, option, Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onNothingSelected(p0: AdapterView<*>?) {
//                TODO("Not yet implemented")
//            }
//
//        }
//    }
    private fun loadDistricts() {
        val spDistricts = findViewById<Spinner>(R.id.spinnerDistritos)
        val call = apiService.getDistricts()

        call.enqueue(object : Callback<ArrayList<District>> {
            override fun onResponse(
                call: Call<ArrayList<District>>,
                response: Response<ArrayList<District>>
            ) {
                if (response.isSuccessful) {
                    val districts = response.body()

                    val districtsOptions = ArrayList<String>()
                    districts?.forEach {
                        districtsOptions.add(it.name)
                    }
                    spDistricts.adapter = ArrayAdapter(
                        this@RegisterRequestSupplierActivity,
                        android.R.layout.simple_list_item_1,
                        districtsOptions
                    )
                }
            }

            override fun onFailure(call: Call<ArrayList<District>>, t: Throwable) {
                Toast.makeText(this@RegisterRequestSupplierActivity, "Ocurrió un problema", Toast.LENGTH_SHORT).show()
                finish()
            }
        })
    }




    private fun loadTypesPayments() {
        val rgTypesPayments = findViewById<RadioGroup>(R.id.rgCondPago) // Referencia al RadioGroup

        val call = apiService.getTypesPayments()
        call.enqueue(object: Callback<ArrayList<TypePayment>> {

            override fun onFailure(call: Call<ArrayList<TypePayment>>, t: Throwable) {
                Toast.makeText(this@RegisterRequestSupplierActivity,
                    "Ocurrió un problema al cargar los tipos de pago del formulario", Toast.LENGTH_SHORT).show()
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

    // Función para validar el formulario
    fun validateForm(): Boolean {
        val etNomProveedor = findViewById<EditText>(R.id.etNomProveedor)
        val etCorreo = findViewById<EditText>(R.id.etCorreo)
        val etDir1 = findViewById<EditText>(R.id.etDir1)
        val etDir2 = findViewById<EditText>(R.id.etDir2)

        val nombreProveedor = etNomProveedor.text.toString()
        val correo = etCorreo.text.toString()
        val dir1 = etDir1.text.toString()
        val dir2 = etDir2.text.toString()

        val rgCondPago = findViewById<RadioGroup>(R.id.rgCondPago)
        val rgTipoPago = findViewById<RadioGroup>(R.id.rgTipoPago)

        val selectedCondPagoId = rgCondPago.checkedRadioButtonId
        val selectedTipoPagoId = rgTipoPago.checkedRadioButtonId

        if (nombreProveedor.isEmpty()) {
            etNomProveedor.error = "El nombre del proveedor no puede estar vacío"
            return false
        } else if (dir1.isEmpty()) {
            etDir1.error = "La dirección 1 no puede estar vacía"
            return false
        } else if (dir2.isEmpty()) {
            etDir2.error = "La dirección 2 no puede estar vacía"
            return false
        } else if (correo.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            etCorreo.error = "Ingresa un correo electrónico válido"
            return false
        } else if (selectedCondPagoId == -1) {
            // Ningún RadioButton seleccionado en el grupo de Condición de Pago
            Toast.makeText(this, "Debes seleccionar una condición de pago", Toast.LENGTH_SHORT).show()
            return false
        } else if (selectedTipoPagoId == -1) {
            // Ningún RadioButton seleccionado en el grupo de Tipo de Pago
            Toast.makeText(this, "Debes seleccionar un tipo de pago", Toast.LENGTH_SHORT).show()
            return false
        }

        return true  // El formulario es válido
    }
}