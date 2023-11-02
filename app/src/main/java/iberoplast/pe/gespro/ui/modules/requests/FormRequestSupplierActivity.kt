package iberoplast.pe.gespro.ui.modules.requests

import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import iberoplast.pe.gespro.R
import iberoplast.pe.gespro.io.ApiService
import iberoplast.pe.gespro.io.response.SimpleResponse
import iberoplast.pe.gespro.model.Countrie
import iberoplast.pe.gespro.model.Document
import iberoplast.pe.gespro.model.MethodPayment
import iberoplast.pe.gespro.model.Policy
import iberoplast.pe.gespro.model.Question
import iberoplast.pe.gespro.model.QuestionResponse
import iberoplast.pe.gespro.model.RequestData
import iberoplast.pe.gespro.model.TypePayment
import iberoplast.pe.gespro.model.ubigeo_peru.Department
import iberoplast.pe.gespro.model.ubigeo_peru.District
import iberoplast.pe.gespro.model.ubigeo_peru.Province
import iberoplast.pe.gespro.util.PreferenceHelper
import iberoplast.pe.gespro.util.PreferenceHelper.get
import iberoplast.pe.gespro.util.toast
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormRequestSupplierActivity : AppCompatActivity() {


    private var currentStep = 1 // Valor predeterminado: paso 1 (20%)
    private lateinit var progressBar: ProgressBar

    private val apiService: ApiService by lazy {
        ApiService.create()
    }

    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }

    private var selectedMethodPayment: String? = null
    private val checkBoxList = mutableListOf<CheckBox>()
    val questionsWithResponses = mutableListOf<QuestionResponse>()
    val listaArchivos = mutableListOf<Document>()


    private lateinit var getContent1: ActivityResultLauncher<String>
    private lateinit var getContent2: ActivityResultLauncher<String>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_request_supplier)

        val btnAdjuntar1 = findViewById<Button>(R.id.btnAdjuntar1)
        val tvAdjuntar1 = findViewById<TextView>(R.id.tvAdjuntar1)

        val btnAdjuntar2 = findViewById<Button>(R.id.btnAdjuntar2)
        val tvAdjuntar2 = findViewById<TextView>(R.id.tvAdjuntar2)

        // Inicializa getContent1 para el botón 1
        getContent1 = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { selectedUri ->
                val fileName = getFileName(selectedUri)
                tvAdjuntar1.text = "Archivo seleccionado: $fileName"
                listaArchivos.add(Document("Título del Archivo 1", fileName, selectedUri))
            }
        }
        // Inicializa getContent2 para el botón 2
        getContent2 = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { selectedUri ->
                val fileName = getFileName(selectedUri)
                tvAdjuntar2.text = "Archivo seleccionado: $fileName"
                listaArchivos.add(Document("Título del Archivo 2", fileName, selectedUri))
            }
        }
        // Configuración de eventos de clic para el botón 1
        btnAdjuntar1.setOnClickListener {
            openGallery(getContent1)
        }
        // Configuración de eventos de clic para el botón 2
        btnAdjuntar2.setOnClickListener {
            openGallery(getContent2)
        }


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
//            val checkBox1 = findViewById<CheckBox>(R.id.checkBox1)
//            val checkBox2 = findViewById<CheckBox>(R.id.checkBox2)
//
//            val aceptoPoliticaPro = checkBox1.isChecked
//            val aceptoPoliticaDatos = checkBox2.isChecked

//            if (!aceptoPoliticaPro && !aceptoPoliticaDatos) {
//                // Manejar la validación de que checkBox1 no está marcado
//                val layout = findViewById<ScrollView>(R.id.layout) // Reemplaza "layout" con el ID de tu diseño
//                val snackbar = Snackbar.make(layout, "Debe aceptar nuestras politicas", Snackbar.LENGTH_LONG)
//                snackbar.show()
//            }
//            else if (!aceptoPoliticaPro) {
//                // Manejar la validación de que checkBox1 no está marcado
//                val layout = findViewById<ScrollView>(R.id.layout) // Reemplaza "layout" con el ID de tu diseño
//                val snackbar = Snackbar.make(layout, "Debe aceptar nuestra politica de proveedores", Snackbar.LENGTH_LONG)
//                snackbar.show()
//            }
//
//            else if (!aceptoPoliticaDatos) {
//                // Manejar la validación de que checkBox1 no está marcado
//                val layout = findViewById<ScrollView>(R.id.layout) // Reemplaza "layout" con el ID de tu diseño
//                val snackbar = Snackbar.make(layout, "Debe aceptar nuestra politica de proteccion de datos", Snackbar.LENGTH_LONG)
//                snackbar.show()
//            }
//            else {
                // Ambos CheckBox están marcados, puedes continuar con tus acciones
                // Continuar con el paso 3(TEST)
                llFormStep3.visibility = View.GONE
                llFormStep4.visibility = View.VISIBLE

                currentStep = 4
                // Actualizar la barra de progreso
                updateProgressBar()
//            }
        }
        // end code formstep3

        // Start code formstep4
        btnNextFormRequest4.setOnClickListener{

            // Continuar con el paso 4(TEST)
            llFormStep4.visibility = View.GONE
            llFormStep5.visibility = View.VISIBLE

            val textViewDatos = findViewById<TextView>(R.id.datos)
            val textViewDatos2 = findViewById<TextView>(R.id.datos2)

            for (archivo in listaArchivos) {
                val titulo = archivo.title
                val nombre = archivo.name
                val ruta = archivo.ruta

                // Construye una cadena con los datos del archivo
                val datosArchivo = "Título: $titulo\nNombre: $nombre\nRuta: $ruta"

                // Asigna la cadena a los TextViews
                if (textViewDatos.text.isEmpty()) {
                    textViewDatos.text = datosArchivo
                } else {
                    textViewDatos2.text = datosArchivo
                }
            }

            currentStep = 5
            // Actualizar la barra de progreso
            updateProgressBar()

        }
        // end code formstep4


        // Start code formstep5
        val btnSend = findViewById<Button>(R.id.btnSend)

        btnSend.setOnClickListener {
            val rgMethodsPayments = findViewById<RadioGroup>(R.id.rgTipoPago) // Referencia al RadioGroup
            // Establece un listener para gestionar la selección
            rgMethodsPayments.setOnCheckedChangeListener { group, checkedId ->
                val selectedRadioButton = findViewById<RadioButton>(checkedId)
                selectedMethodPayment = selectedRadioButton.text.toString()
                // Hacer algo con el tipo de pago seleccionado si es necesario aquí
            }
            performStoreSupplierRequest(selectedMethodPayment)
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
        loadPolicies()
        loadQuestions()
        setupRadioGroup()
        loadCountries(true)
        loadDepartments()
        listenDepartmentChanges()
    }

    // Función para abrir la galería y seleccionar un archivo
    private fun openGallery(getContent: ActivityResultLauncher<String>) {
        getContent.launch("*/*")
    }
    private fun getFileName(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val displayNameColumnIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (displayNameColumnIndex != -1) {
                    val displayName = it.getString(displayNameColumnIndex)
                    if (displayName != null) {
                        return displayName
                    }
                }
            }
        }
        return "Archivo no encontrado"
    }

    private fun performStoreSupplierRequest(selectedMethodPayment: String?) {
        val btnConfirm = findViewById<Button>(R.id.btnSend)
        btnConfirm.isClickable = false

        val jwt = preferences["jwt", ""]
        val authHeader = "Bearer $jwt"
        val spCountry = findViewById<Spinner>(R.id.spinnerPaises)
        val nationality = spCountry.selectedItem.toString()
//
        val nic_ruc = findViewById<EditText>(R.id.etNicRuc).text.toString() // Obtén el valor del EditText
        val nameSupplier = findViewById<EditText>(R.id.etNomProveedor).text.toString()
        val emailSupplier = findViewById<EditText>(R.id.etCorreo).text.toString()
//        val locality = findViewById<EditText>(R.id.etDir1).text.toString()
//        val streetNumber = findViewById<EditText>(R.id.etDir2).text.toString()

        val spConPayment = findViewById<Spinner>(R.id.spinnerCondPago)
        val typePayment = spConPayment.selectedItem.toString()
        val methodPayment = selectedMethodPayment

        val selectedPolicies = ArrayList<Policy>()
        for (checkBox in checkBoxList) {
            if (checkBox.isChecked) {
                val id = checkBox.id
                val title = checkBox.text.toString()
                val isChecked = checkBox.isChecked
                selectedPolicies.add(Policy(id, title, "", isChecked))
            }
        }

        val requestData = RequestData(selectedPolicies, questionsWithResponses)

        val files = mutableListOf<MultipartBody.Part>()

        for (archivo in listaArchivos) {
            val nombreArchivo = archivo.title  // Obtén el nombre del archivo utilizando tu función
            val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "")
            val part = MultipartBody.Part.createFormData("files[]", nombreArchivo, requestFile)
            files.add(part)
        }

        val call = apiService.postSupplierRequests(
            authHeader,
            nationality,
            nic_ruc,
            nameSupplier,
            emailSupplier,
            typePayment,
            methodPayment,
            requestData
            )
        call.enqueue(object: Callback<SimpleResponse>{
            override fun onResponse(
                call: Call<SimpleResponse>,
                response: Response<SimpleResponse>
            ) {
                if (response.isSuccessful){
                    toast("Enviado")

                    finish()
                } else {
                    toast("Ocurrio un error inesperado en el registro de la solicitud")
                    btnConfirm.isClickable = true
                }
            }

            override fun onFailure(call: Call<SimpleResponse>, t: Throwable) {
                t.localizedMessage?.let { toast(it) }
                btnConfirm.isClickable = true
            }

        })
        //val mensaje = "Su solicitud fue enviada satisfactoriamente."
        //val intent = Intent(this, SuccesfulRegisterActivity::class.java)
        //intent.putExtra("mensaje", mensaje) // Configura el mensaje en la intención antes de iniciar la actividad
        //startActivity(intent)
        //Finalizar la actividad actual
        //finish()
    }

    private fun setupRadioGroup() {
        val radioGroup = findViewById<RadioGroup>(R.id.rgTipoProveedor)
        val spinnerDepartamentos = findViewById<Spinner>(R.id.spinnerDepartamentos)
        val spinnerProvincias = findViewById<Spinner>(R.id.spinnerProvincias)
        val spinnerDistritos = findViewById<Spinner>(R.id.spinnerDistritos)
        val dirNacional = findViewById<LinearLayout>(R.id.dirNacional)
        val dirAdicNacional = findViewById<LinearLayout>(R.id.dirAdicNacional)
        val dirExtranjero = findViewById<LinearLayout>(R.id.dirExtranjero)

        // Configurar el RadioGroup para manejar los cambios
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            // Lógica según la selección
            when (checkedId) {
                R.id.rbNacional -> {
                    dirNacional.visibility = View.VISIBLE
                    dirAdicNacional.visibility = View.VISIBLE
                    dirExtranjero.visibility = View.GONE
                    loadCountries(true)
                }
                R.id.rbExtranjero -> {
                    dirNacional.visibility = View.GONE
                    dirAdicNacional.visibility = View.GONE
                    dirExtranjero.visibility = View.VISIBLE
                    loadCountries(false)
                }
            }
        }
    }
    private fun loadCountries(isNacional: Boolean) {
        val spCountries = findViewById<Spinner>(R.id.spinnerPaises)
        val rbNacional = findViewById<RadioButton>(R.id.rbNacional)
        val rbExtranjero = findViewById<RadioButton>(R.id.rbExtranjero)

        val call = apiService.getCountries()
        call.enqueue(object : Callback<ArrayList<Countrie>> {
            override fun onResponse(
                call: Call<ArrayList<Countrie>>,
                response: Response<ArrayList<Countrie>>
            ) {
                if (response.isSuccessful) {
                    val countries = response.body() ?: ArrayList() // Convierte a una lista

                    // Crear un ArrayAdapter para el Spinner con la lista de países
                    val adapter = ArrayAdapter(
                        this@FormRequestSupplierActivity,
                        android.R.layout.simple_list_item_1,
                        countries
                    )

                    // Asignar el ArrayAdapter al Spinner
                    spCountries.adapter = adapter

                    // Configurar el Listener para el Spinner
                    spCountries.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            val selectedCountry = countries[position]

                            if (selectedCountry.name == "Perú") {
                                // Si se selecciona "Perú" en el Spinner, marca el RadioButton Nacional
                                rbNacional.isChecked = true
                            } else {
                                // Si se selecciona cualquier otro país en el Spinner, marca el RadioButton Extranjero
                                rbExtranjero.isChecked = true
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            // Puedes manejar esto según tus necesidades
                        }
                    }

                    if (isNacional) {
                        // Si es "Nacional," mostrar el Spinner con "Perú" seleccionado
                        val peruIndex = countries.indexOfFirst { it.name == "Perú" }
                        spCountries.setSelection(peruIndex)
                    }
                } else {
                    // Manejar la respuesta no exitosa aquí
                }
            }

            override fun onFailure(call: Call<ArrayList<Countrie>>, t: Throwable) {
                // Manejar la falla en la solicitud aquí
            }
        })
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
                    val departments = response.body() ?: ArrayList() // Convierte a una lista

                    spDepartamentos.adapter = ArrayAdapter(
                        this@FormRequestSupplierActivity,
                        android.R.layout.simple_list_item_1,
                        departments
                    )
                }
            }
            override fun onFailure(call: Call<ArrayList<Department>>, t: Throwable) {
                Toast.makeText(this@FormRequestSupplierActivity, "Ocurrió un problema", Toast.LENGTH_SHORT).show()
                finish()
            }
        })
    }


    private fun listenDepartmentChanges() {
        val spDepartments = findViewById<Spinner>(R.id.spinnerDepartamentos)
        val spProvinces = findViewById<Spinner>(R.id.spinnerProvincias)
        val spDistricts = findViewById<Spinner>(R.id.spinnerDistritos)

        spDepartments.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val department = parent?.getItemAtPosition(position) as Department
                val departmentId = department.id

                // Limpiar los Spinners de Provincias y Distritos al seleccionar un nuevo departamento
                spProvinces.adapter = null
                spDistricts.adapter = null

                loadProvinces(departmentId)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Implementar la lógica en caso de que no se seleccione nada si es necesario
            }
        }

        spProvinces.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val province = parent?.getItemAtPosition(position) as Province

                val provinceId = province.id

                spDistricts.adapter = null

                loadDistricts(provinceId)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Implementar la lógica en caso de que no se seleccione nada si es necesario
            }
        }
    }

    private fun loadProvinces(departmentId: String) {
        val spProvinces = findViewById<Spinner>(R.id.spinnerProvincias)
        val call = apiService.getProvinces(departmentId)

        call.enqueue(object: Callback<ArrayList<Province>> {
            override fun onResponse(
                call: Call<ArrayList<Province>>,
                response: Response<ArrayList<Province>>
            ) {
                if (response.isSuccessful) {

                    val provinces = response.body() ?: ArrayList()
                    spProvinces.adapter = ArrayAdapter(
                        this@FormRequestSupplierActivity,
                        android.R.layout.simple_list_item_1,
                        provinces
                    )
                }
            }
            override fun onFailure(call: Call<ArrayList<Province>>, t: Throwable) {
                Toast.makeText(this@FormRequestSupplierActivity, "Ocurrió un problema", Toast.LENGTH_SHORT).show()
                finish()
            }
        })
    }


    private fun loadDistricts(provinceId: String) {
        val spDistricts = findViewById<Spinner>(R.id.spinnerDistritos)
        val call = apiService.getDistricts(provinceId)

        call.enqueue(object: Callback<ArrayList<District>> {
            override fun onResponse(
                call: Call<ArrayList<District>>,
                response: Response<ArrayList<District>>
            ) {
                if (response.isSuccessful) {
                    val districts = response.body()
                    if (districts != null) {
                        val districtList: List<District> = districts
                        spDistricts.adapter = ArrayAdapter(
                            this@FormRequestSupplierActivity,
                            android.R.layout.simple_list_item_1,
                            districtList
                        )
                    } else {
                        Log.e("API Response", "La lista ha devuelto null")
                    }
                } else {
                    Log.e("API Response", "Response no responde")
                }
            }
            override fun onFailure(call: Call<ArrayList<District>>, t: Throwable) {
                Toast.makeText(this@FormRequestSupplierActivity, "Ocurrió un problema", Toast.LENGTH_SHORT).show()
                finish()
            }
        })
    }

    private fun loadTypesPayments() {
//        val rgTypesPayments = findViewById<RadioGroup>(R.id.rgCondPago) // Referencia al RadioGroup
        val spinnerCondPago = findViewById<Spinner>(R.id.spinnerCondPago)


        val call = apiService.getTypesPayments()
        call.enqueue(object: Callback<ArrayList<TypePayment>> {
            override fun onResponse(
                call: Call<ArrayList<TypePayment>>,
                response: Response<ArrayList<TypePayment>>
            ) {
                if (response.isSuccessful) {
                    val typesPayments = response.body()

                    if (typesPayments != null) {
                        // Crea un ArrayAdapter para el Spinner
                        val adapter = ArrayAdapter(
                            this@FormRequestSupplierActivity,
                            android.R.layout.simple_spinner_item,
                            typesPayments.map { it.name }
                        )

                        // Especifica el diseño para el menú desplegable
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                        // Asigna el ArrayAdapter al Spinner
                        spinnerCondPago.adapter = adapter
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<TypePayment>>, t: Throwable) {
                Toast.makeText(this@FormRequestSupplierActivity,
                    "Ocurrió un problema al cargar los tipos de pago del formulario", Toast.LENGTH_SHORT).show()
                finish()
            }
        })

    }

    private fun loadMethodsPayments() {
        val rgMethodsPayments = findViewById<RadioGroup>(R.id.rgTipoPago) // Referencia al RadioGroup

        val call = apiService.getMethodsPayments()
        call.enqueue(object: Callback<ArrayList<MethodPayment>> {

            override fun onFailure(call: Call<ArrayList<MethodPayment>>, t: Throwable) {
                Toast.makeText(this@FormRequestSupplierActivity, "Ocurrió un problema al cargar los tipos de pago del formulario", Toast.LENGTH_SHORT).show()
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
                            val radioButton = RadioButton(this@FormRequestSupplierActivity)
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
                            selectedMethodPayment = selectedRadioButton.text.toString()
                            // Hacer algo con el tipo de pago seleccionado
                        }
                    }
                }
            }
        })
    }
    private fun loadPolicies(){
        val call = apiService.getPolicies()
        call.enqueue(object : Callback<ArrayList<Policy>> {
            override fun onResponse(call: Call<ArrayList<Policy>>, response: Response<ArrayList<Policy>>) {
                if (response.isSuccessful) {
                    val policies = response.body()
                    val policyContainer = findViewById<LinearLayout>(R.id.policyContainer)
                    if (policies != null) {
                        for (policy in policies) {
                            val checkBox = CheckBox(this@FormRequestSupplierActivity)
                            checkBox.id = policy.id
                            checkBox.text = policy.title

                            // Configura las propiedades de layout
                            val layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                            checkBox.layoutParams = layoutParams

                            policyContainer.addView(checkBox)
                            checkBoxList.add(checkBox)

                            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                                val policyId = buttonView.id
                            }
                        }
                    }
                } else {
                    // Manejar una respuesta de error del servidor
                }
            }

            override fun onFailure(call: Call<ArrayList<Policy>>, t: Throwable) {
                // Manejar errores de red o problemas de conexión
            }
        })
    }

    private fun loadQuestions() {
        val call = apiService.getQuestions()
        call.enqueue(object : Callback<ArrayList<Question>> {
            override fun onResponse(
                call: Call<ArrayList<Question>>,
                response: Response<ArrayList<Question>>
            ) {
                if (response.isSuccessful) {
                    val preguntas = response.body()

                    if (preguntas != null) {
                        for (pregunta in preguntas) {
                            val questionLayout = LinearLayout(this@FormRequestSupplierActivity)
                            questionLayout.layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                            questionLayout.orientation = LinearLayout.HORIZONTAL

                            val preguntaTextView = TextView(this@FormRequestSupplierActivity)
                            preguntaTextView.layoutParams = LinearLayout.LayoutParams(
                                0,
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                1.0f
                            )
                            preguntaTextView.text = pregunta.question

                            val radioGroup = RadioGroup(this@FormRequestSupplierActivity)
                            radioGroup.layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                            radioGroup.orientation = RadioGroup.HORIZONTAL

                            val siRadioButton = RadioButton(this@FormRequestSupplierActivity)
                            siRadioButton.layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                            siRadioButton.text = "Si"
                            siRadioButton.id = View.generateViewId() // Asignar un ID único a siRadioButton
                            siRadioButton.isChecked = true

                            val noRadioButton = RadioButton(this@FormRequestSupplierActivity)
                            noRadioButton.layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                            noRadioButton.text = "No"
                            noRadioButton.id = View.generateViewId() // Asignar un ID único a noRadioButton

                            radioGroup.setOnCheckedChangeListener { group, checkedId ->
                                val preguntaId = pregunta.id
                                val respuesta = when (checkedId) {
                                    siRadioButton.id -> true
                                    noRadioButton.id -> false
                                    else -> false  // Manejo de errores o valor predeterminado
                                }
                                val questionWithResponse = QuestionResponse(preguntaId, respuesta)
                                questionsWithResponses.add(questionWithResponse)
                            }

                            // Agrega los RadioButtons al RadioGroup
                            radioGroup.addView(siRadioButton)
                            radioGroup.addView(noRadioButton)

                            questionLayout.addView(preguntaTextView)
                            questionLayout.addView(radioGroup)

                            val questionContainer = findViewById<LinearLayout>(R.id.questionContainer)
                            questionContainer.addView(questionLayout)

                        }
                    }

                }
            }

            override fun onFailure(call: Call<ArrayList<Question>>, t: Throwable) {
                TODO("Not yet implemented")
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

        val rgTipoPago = findViewById<RadioGroup>(R.id.rgTipoPago)

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
        } else if (selectedTipoPagoId == -1) {
            // Ningún RadioButton seleccionado en el grupo de Tipo de Pago
            Toast.makeText(this, "Debes seleccionar un tipo de pago", Toast.LENGTH_SHORT).show()
            return false
        }

        return true  // El formulario es válido
    }
}