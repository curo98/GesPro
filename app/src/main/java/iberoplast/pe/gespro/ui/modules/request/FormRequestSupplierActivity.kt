package iberoplast.pe.gespro.ui.modules.request

import android.annotation.SuppressLint
import android.content.Intent
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
import android.widget.Filter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.ScrollView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.toptoche.searchablespinnerlibrary.SearchableSpinner
import iberoplast.pe.gespro.R
import iberoplast.pe.gespro.io.ApiService
import iberoplast.pe.gespro.io.PdfFragmentListener
import iberoplast.pe.gespro.io.response.SimpleResponse
import iberoplast.pe.gespro.model.Countrie
import iberoplast.pe.gespro.model.DocumentLoadToServer
import iberoplast.pe.gespro.model.MethodPayment
import iberoplast.pe.gespro.model.Policy
import iberoplast.pe.gespro.model.PolicyPivot
import iberoplast.pe.gespro.model.Question
import iberoplast.pe.gespro.model.QuestionResponse
import iberoplast.pe.gespro.model.RequestData
import iberoplast.pe.gespro.model.SupplierRequest
import iberoplast.pe.gespro.model.TypePayment
import iberoplast.pe.gespro.ui.modules.SuccesfulRegisterActivity
import iberoplast.pe.gespro.util.ActionBarUtils
import iberoplast.pe.gespro.util.PdfFragment
import iberoplast.pe.gespro.util.PreferenceHelper
import iberoplast.pe.gespro.util.PreferenceHelper.get
import iberoplast.pe.gespro.util.PreferenceHelper.set
import iberoplast.pe.gespro.util.toast
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormRequestSupplierActivity : AppCompatActivity(), PdfFragmentListener {

    private val etNicRuc by lazy { findViewById<EditText>(R.id.etNicRuc) }
    private val etDir2 by lazy { findViewById<EditText>(R.id.etDir2) }
    private val etDir1 by lazy { findViewById<EditText>(R.id.etDir1) }
    private val btnSend by lazy { findViewById<Button>(R.id.btnSend) }

    private val tvTittleCodeEtic by lazy { findViewById<TextView>(R.id.tvTittleCodeEtic) }
    private val tvTitleConst by lazy { findViewById<TextView>(R.id.tvTitleConst) }
    private val tvTittleRepres by lazy { findViewById<TextView>(R.id.tvTittleRepres) }
    private val tvTitleCart by lazy { findViewById<TextView>(R.id.tvTitleCart) }
    private val tvTitleRuc by lazy { findViewById<TextView>(R.id.tvTitleRuc) }

    private val ivDoc1 by lazy { findViewById<ImageView>(R.id.ivDoc1) }
    private val ivDoc2 by lazy { findViewById<ImageView>(R.id.ivDoc2) }
    private val ivDoc3 by lazy { findViewById<ImageView>(R.id.ivDoc3) }
    private val ivDoc4 by lazy { findViewById<ImageView>(R.id.ivDoc4) }
    private val ivDoc5 by lazy { findViewById<ImageView>(R.id.ivDoc5) }

    private var currentStep = 1 // Valor predeterminado: paso 1 (20%)
    private lateinit var progressBar: ProgressBar

    private val apiService: ApiService by lazy {
        ApiService.create()
    }
    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this@FormRequestSupplierActivity)
    }

    private var methodPayment: String? = null
    private val checkBoxList = mutableListOf<CheckBox>()
    val questionsWithResponses = mutableListOf<QuestionResponse>()
    val listaArchivos = mutableListOf<DocumentLoadToServer>()


    private lateinit var getContent1: ActivityResultLauncher<String>
    private lateinit var getContent2: ActivityResultLauncher<String>
    private lateinit var getContent3: ActivityResultLauncher<String>
    private lateinit var getContent4: ActivityResultLauncher<String>
    private lateinit var getContent5: ActivityResultLauncher<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_request_supplier)


        val btnAdjuntar1 = findViewById<Button>(R.id.btnAdjuntar1)
        val tvAdjuntar1 = findViewById<TextView>(R.id.tvAdjuntar1)

        val btnAdjuntar2 = findViewById<Button>(R.id.btnAdjuntar2)
        val tvAdjuntar2 = findViewById<TextView>(R.id.tvAdjuntar2)

        val btnAdjuntar3 = findViewById<Button>(R.id.btnAdjuntar3)
        val tvAdjuntar3 = findViewById<TextView>(R.id.tvAdjuntar3)

        val btnAdjuntar4 = findViewById<Button>(R.id.btnAdjuntar4)
        val tvAdjuntar4 = findViewById<TextView>(R.id.tvAdjuntar4)

        val btnAdjuntar5 = findViewById<Button>(R.id.btnAdjuntar5)
        val tvAdjuntar5 = findViewById<TextView>(R.id.tvAdjuntar5)

        // Inicializa getContent1 para el botón 1
        getContent1 = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { selectedUri ->
                if (isPdfFile(selectedUri)) {
                    val title = tvTittleCodeEtic.text.toString()
                    val fileName = getFileName(selectedUri)
                    val fileContent = getContentBytes(selectedUri)
                    val fileData = DocumentLoadToServer(title, fileName, fileContent)
                    tvAdjuntar1.text = "Archivo seleccionado: $fileName"
                    listaArchivos.add(fileData)
                } else {
                    // If it's not a PDF file, display a Toast message indicating the restriction
                    toast("Solo se permite archivos PDF")
                }
            }
        }

        // Inicializa getContent2 para el botón 2
        getContent2 = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { selectedUri ->
                if (isPdfFile(selectedUri)) {
                    val title = tvTitleConst.text.toString()
                    val fileName = getFileName(selectedUri)
                    val fileContent = getContentBytes(selectedUri)
                    val fileData = DocumentLoadToServer(title, fileName, fileContent)
                    tvAdjuntar2.text = "Archivo seleccionado: $fileName"
                    listaArchivos.add(fileData)
                } else {
                    // If it's not a PDF file, display a Toast message indicating the restriction
                    toast("Solo se permite archivos PDF")
                }

            }
        }

        // Inicializa getContent2 para el botón 2
        getContent3 = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { selectedUri ->
                if (isPdfFile(selectedUri)) {
                    val title = tvTittleRepres.text.toString()
                    val fileName = getFileName(selectedUri)
                    val fileContent = getContentBytes(selectedUri)
                    val fileData = DocumentLoadToServer(title, fileName, fileContent)
                    tvAdjuntar3.text = "Archivo seleccionado: $fileName"
                    listaArchivos.add(fileData)
                } else {
                    // If it's not a PDF file, display a Toast message indicating the restriction
                    toast("Solo se permite archivos PDF")
                }

            }
        }

        // Inicializa getContent2 para el botón 2
        getContent4 = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { selectedUri ->
                if (isPdfFile(selectedUri)) {
                    val title = tvTitleCart.text.toString()
                    val fileName = getFileName(selectedUri)
                    val fileContent = getContentBytes(selectedUri)
                    val fileData = DocumentLoadToServer(title, fileName, fileContent)
                    tvAdjuntar4.text = "Archivo seleccionado: $fileName"
                    listaArchivos.add(fileData)
                } else {
                    // If it's not a PDF file, display a Toast message indicating the restriction
                    toast("Solo se permite archivos PDF")
                }

            }
        }

        // Inicializa getContent2 para el botón 2
        getContent5 = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { selectedUri ->
                if (isPdfFile(selectedUri)) {
                    val title = tvTitleRuc.text.toString()
                    val fileName = getFileName(selectedUri)
                    val fileContent = getContentBytes(selectedUri)
                    val fileData = DocumentLoadToServer(title, fileName, fileContent)
                    tvAdjuntar5.text = "Archivo seleccionado: $fileName"
                    listaArchivos.add(fileData)
                } else {
                    // If it's not a PDF file, display a Toast message indicating the restriction
                    toast("Solo se permite archivos PDF")
                }

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

        // Configuración de eventos de clic para el botón 3
        btnAdjuntar3.setOnClickListener {
            openGallery(getContent3)
        }
        // Configuración de eventos de clic para el botón 4
        btnAdjuntar4.setOnClickListener {
            openGallery(getContent4)
        }

        // Configuración de eventos de clic para el botón 5
        btnAdjuntar5.setOnClickListener {
            openGallery(getContent5)
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
        btnNextFormRequest1.setOnClickListener {
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

        val url =
            "https://drive.google.com/file/d/1QMWS8IJD3iTIxUFfl4GX5WD7JhXRRuK1/view?usp=sharing"
        // Reemplaza "ENLACE_PDF" con el enlace de Google Drive a tu archivo PDF

        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        webView.loadUrl(url)

        btnNextFormRequest3.setOnClickListener {
            // Obtener los CheckBoxes seleccionados directamente en el onClickListener
            val selectedCheckBoxes = checkBoxList.filter { it.isChecked }

            // Realizar validación
            if (selectedCheckBoxes.isEmpty()) {
                // Ningún CheckBox está marcado
                val layout =
                    findViewById<ScrollView>(R.id.layout) // Reemplaza "layout" con el ID de tu diseño
                val snackbar = Snackbar.make(
                    layout,
                    "Debe aceptar al menos una política",
                    Snackbar.LENGTH_LONG
                )
                snackbar.show()
            } else {
                // Al menos un CheckBox está marcado, puedes continuar con tus acciones
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
        btnNextFormRequest4.setOnClickListener {
            val checkBox1 = findViewById<CheckBox>(R.id.checkBox1)
            if (checkBox1.isChecked()) {
                // Continuar con el paso 4(TEST)
                llFormStep4.visibility = View.GONE
                llFormStep5.visibility = View.VISIBLE
                currentStep = 5
                // Actualizar la barra de progreso
                updateProgressBar()
            } else {
                toast("Debe aceptar el cumplimiento de código de ética")
            }
        }
        // end code formstep4


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

        // Obtén el valor de "isEditing" del intent
        val isEditing = intent.getBooleanExtra("isEditing", false)
        val request = intent.getParcelableExtra<SupplierRequest>("request")

        val rgMethodsPayments =
            findViewById<RadioGroup>(R.id.rgTipoPago) // Referencia al RadioGroup
        // Establece un listener para gestionar la selección
        rgMethodsPayments.setOnCheckedChangeListener { group, checkedId ->
            val selectedRadioButton = findViewById<RadioButton>(checkedId)
            methodPayment = selectedRadioButton.text.toString()
            // Hacer algo con el tipo de pago seleccionado si es necesario aquí
        }

        // Si isEditing es true, estás editando un proveedor, muestra los datos existentes
        if (isEditing && request != null) {
            ActionBarUtils.setCustomTitle(
                this,
                "Editando solicitud con id: ${request.id}"
            )
            etNicRuc.setText(request.user.supplier?.nic_ruc)
            etDir2.setText(request.user.supplier?.locality)
            etDir1.setText(request.user.supplier?.street_and_number)

            for (document in request.documents) {
                val title = document.title
                val name = document.name

                when (title) {
                    tvTittleCodeEtic.text.toString() -> {
                        // Coincide con tvTittleCodeEtic, imprimir en tvAdjuntar1
                        ivDoc1.visibility = View.VISIBLE
                        tvAdjuntar1.text = "Archivo seleccionado: $name"
                        ivDoc1.setOnClickListener {
                            val llFormStep4 = findViewById<LinearLayout>(R.id.llFormStep4)
                            llFormStep4.visibility = View.GONE
                            val uri = document.uri

                            // Concatenar el dominio con la URI para formar la URL completa
                            val dominio = "https://gespro-iberoplast.tech"
                            val pdf_url = "$dominio$uri"

                            val pdfFragment = PdfFragment.newInstance("${pdf_url}")
                            pdfFragment.setPdfFragmentListener(this)
                            // Utiliza replace en lugar de add
                            supportFragmentManager.beginTransaction()
                                .replace(android.R.id.content, pdfFragment)
                                .addToBackStack(null)
                                .commit()
                        }
                    }
                    tvTitleConst.text.toString() -> {
                        ivDoc2.visibility = View.VISIBLE
                        // Coincide con tvTitleConst, imprimir en tvAdjuntar2
                        tvAdjuntar2.text = "Archivo seleccionado: $name"
                        ivDoc2.setOnClickListener {
                            val llFormStep4 = findViewById<LinearLayout>(R.id.llFormStep4)
                            llFormStep4.visibility = View.GONE
                            val uri = document.uri

                            // Concatenar el dominio con la URI para formar la URL completa
                            val dominio = "https://gespro-iberoplast.tech"
                            val pdf_url = "$dominio$uri"

                            val pdfFragment = PdfFragment.newInstance("${pdf_url}")
                            pdfFragment.setPdfFragmentListener(this)
                            // Utiliza replace en lugar de add
                            supportFragmentManager.beginTransaction()
                                .replace(android.R.id.content, pdfFragment)
                                .addToBackStack(null)
                                .commit()
                        }
                    }
                    tvTittleRepres.text.toString() -> {
                        ivDoc3.visibility = View.VISIBLE
                        // Coincide con tvTittleRepres, imprimir en tvAdjuntar3
                        tvAdjuntar3.text = "Archivo seleccionado: $name"
                        ivDoc3.setOnClickListener {
                            val llFormStep4 = findViewById<LinearLayout>(R.id.llFormStep4)
                            llFormStep4.visibility = View.GONE
                            val uri = document.uri

                            // Concatenar el dominio con la URI para formar la URL completa
                            val dominio = "https://gespro-iberoplast.tech"
                            val pdf_url = "$dominio$uri"

                            val pdfFragment = PdfFragment.newInstance("${pdf_url}")
                            pdfFragment.setPdfFragmentListener(this)
                            // Utiliza replace en lugar de add
                            supportFragmentManager.beginTransaction()
                                .replace(android.R.id.content, pdfFragment)
                                .addToBackStack(null)
                                .commit()
                        }
                    }
                    tvTitleCart.text.toString() -> {
                        ivDoc4.visibility = View.VISIBLE
                        // Coincide con tvTittleRepres, imprimir en tvAdjuntar4
                        tvAdjuntar4.text = "Archivo seleccionado: $name"
                        ivDoc4.setOnClickListener {
                            val llFormStep4 = findViewById<LinearLayout>(R.id.llFormStep4)
                            llFormStep4.visibility = View.GONE
                            val uri = document.uri

                            // Concatenar el dominio con la URI para formar la URL completa
                            val dominio = "https://gespro-iberoplast.tech"
                            val pdf_url = "$dominio$uri"

                            val pdfFragment = PdfFragment.newInstance("${pdf_url}")
                            pdfFragment.setPdfFragmentListener(this)
                            // Utiliza replace en lugar de add
                            supportFragmentManager.beginTransaction()
                                .replace(android.R.id.content, pdfFragment)
                                .addToBackStack(null)
                                .commit()
                        }
                    }
                    tvTitleRuc.text.toString() -> {
                        ivDoc5.visibility = View.VISIBLE
                        // Coincide con tvTittleRepres, imprimir en tvAdjuntar5
                        tvAdjuntar5.text = "Archivo seleccionado: $name"
                        ivDoc5.setOnClickListener {
                            val llFormStep4 = findViewById<LinearLayout>(R.id.llFormStep4)
                            llFormStep4.visibility = View.GONE
                            val uri = document.uri

                            // Concatenar el dominio con la URI para formar la URL completa
                            val dominio = "https://gespro-iberoplast.tech"
                            val pdf_url = "$dominio$uri"

                            val pdfFragment = PdfFragment.newInstance("${pdf_url}")
                            pdfFragment.setPdfFragmentListener(this)
                            // Utiliza replace en lugar de add
                            supportFragmentManager.beginTransaction()
                                .replace(android.R.id.content, pdfFragment)
                                .addToBackStack(null)
                                .commit()
                        }
                    }
                }
            }
            loadCountries(request)
            loadTypesPayments(request)
            loadMethodsPayments(request)
            loadPolicies(request)
            loadQuestions(request)

            btnSend.setOnClickListener {
                val id = request.id
                executeMethodUpdate(id, methodPayment)
            }
        } else {
            ActionBarUtils.setCustomTitle(
                this,
                "Registro de nueva solicitud"
            )
            loadCountries(null)
            loadTypesPayments(null)
            loadMethodsPayments(null)
            loadPolicies(null)
            loadQuestions(null)

            btnSend.setOnClickListener {
                if (!validateQuestionResponses()) {
                    return@setOnClickListener
                }
                executeMethodCreate(methodPayment)
            }
        }
    }
    override fun onCloseButtonClicked() {
        val llFormStep4 = findViewById<LinearLayout>(R.id.llFormStep4)
        llFormStep4.visibility = View.VISIBLE
    }
    private fun validateQuestionResponses(): Boolean {
        val questionContainer = findViewById<LinearLayout>(R.id.questionContainer)

        for (i in 0 until questionContainer.childCount) {
            val questionLayout = questionContainer.getChildAt(i) as LinearLayout
            val radioGroup = questionLayout.getChildAt(1) as RadioGroup

            if (radioGroup.checkedRadioButtonId == -1) {
                // Ningún RadioButton seleccionado en este grupo de preguntas
                toast("Por favor, responde todas las preguntas")
                return false
            }
        }

        return true
    }
    private fun openGallery(getContent: ActivityResultLauncher<String>) {
        // Lanzar la actividad de selección de archivos
        getContent.launch("application/pdf")
    }
    private fun isPdfFile(uri: Uri): Boolean {
        val type = contentResolver.getType(uri)
        return type?.startsWith("application/pdf") == true
    }
    private fun getFileName(uri: Uri): String {
        // Obtener el nombre del archivo desde su URI
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

    private fun getContentBytes(uri: Uri): ByteArray {
        // Obtener los bytes del contenido del archivo
        val inputStream = contentResolver.openInputStream(uri)
        return inputStream?.readBytes() ?: byteArrayOf()
    }
    private fun sendFiles() {
        val jwt = preferences["jwt", ""]
        val authHeader = "Bearer $jwt"

        for (fileData in listaArchivos) {
            Log.d("FileUpload", "Archivo a enviar: ${fileData.filename}")
        }

        // Imprime el contenido de listaArchivos
        for (fileData in listaArchivos) {
            Log.d("MisArchivosCargados", "Elemento de listaArchivos: Title=${fileData.title}, Filename=${fileData.filename}, FileContent=${fileData.fileContent.size} bytes")
        }
        val fileParts = mutableListOf<MultipartBody.Part>()

        for (fileData in listaArchivos) {

            val fileRequestBody = fileData.fileContent.toRequestBody("application/octet-stream".toMediaTypeOrNull())
            val filePart = MultipartBody.Part.createFormData("files[]", fileData.filename, fileRequestBody)

            val titlePart = MultipartBody.Part.createFormData("titles[]", fileData.title)
            fileParts.add(filePart)
            fileParts.add(titlePart)
        }

        val call = apiService.uploadFiles(authHeader, fileParts)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("FileUpload", "Archivos enviados exitosamente")
                } else {
                    Log.e("FileUpload", "Error en la respuesta del servidor: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("FileUpload", "Error en la solicitud: ${t.message}")
            }
        })
    }

    private fun executeMethodCreate(methodPayment: String?) {
        val btnConfirm = findViewById<Button>(R.id.btnSend)
//        btnConfirm.isClickable = false
        val jwt = preferences["jwt", ""]
        val authHeader = "Bearer $jwt"
        val spCountry = findViewById<Spinner>(R.id.spinnerPaises)
        val nationality = spCountry.selectedItem.toString()
        val nic_ruc =
            findViewById<EditText>(R.id.etNicRuc).text.toString() // Obtén el valor del EditText
        val locality =
            findViewById<EditText>(R.id.etDir2).text.toString() // Obtén el valor del EditText
        val street_and_number =
            findViewById<EditText>(R.id.etDir1).text.toString() // Obtén el valor del EditText
        val spConPayment = findViewById<Spinner>(R.id.spinnerCondPago)
        val typePayment = spConPayment.selectedItem.toString()

        val selectedPolicies = ArrayList<Policy>()
        for (checkBox in checkBoxList) {
            if (checkBox.isChecked) {
                val id = checkBox.id // Asumiendo que el ID proviene del checkbox
                val title = checkBox.text.toString()
                val content = "" // Debe ser inicializado con un valor válido, según tu modelo
                val isChecked = checkBox.isChecked
                val pivot = PolicyPivot(0, 0, 0) // Debe ser inicializado según tu modelo

                selectedPolicies.add(Policy(id, title, content, isChecked, pivot))
            }
        }
        val requestData = RequestData(selectedPolicies, questionsWithResponses)

        Log.d("FormRequestSupplier", "Selected Policies: $selectedPolicies")

        // Imprimir questionsWithResponses en el log
        Log.d("FormRequestSupplier", "Questions with Responses: $questionsWithResponses")


        val call = apiService.postSupplierRequests(
            authHeader,
            nationality,
            nic_ruc,
            locality,
            street_and_number,
            typePayment,
            methodPayment,
            requestData
        )
        call.enqueue(object : Callback<SimpleResponse> {
            override fun onResponse(
                call: Call<SimpleResponse>,
                response: Response<SimpleResponse>
            ) {
                if (response.isSuccessful) {

                    sendFiles()

                    preferences["UserRolePreferences"] = "proveedor"
                    val message = "Su solicitud fue enviada satisfactoriamente."
                    val intent = Intent(
                        this@FormRequestSupplierActivity,
                        SuccesfulRegisterActivity::class.java
                    )
                    intent.putExtra(
                        "message",
                        message
                    ) // Configura el mensaje en la intención antes de iniciar la actividad
                    startActivity(intent)
                    finish()
                } else {
                    toast("Ocurrio un error inesperado en el registro de la solicitud")
//                    btnConfirm.isClickable = true
                }
            }

            override fun onFailure(call: Call<SimpleResponse>, t: Throwable) {
                t.localizedMessage?.let { toast(it) }
//                btnConfirm.isClickable = true
            }

        })

    }
    private fun sendUpdateFiles(id: Int)
    {
        val jwt = preferences["jwt", ""]
        val authHeader = "Bearer $jwt"

        for (fileData in listaArchivos) {
            Log.d("FileUpdatedUpload", "Archivo actualizado a enviar: ${fileData.filename}")
        }

        // Imprime el contenido de listaArchivos
        for (fileData in listaArchivos) {
            Log.d("MisArchivosActualizadosCargados", "Elemento de listaArchivos actualizada: Title=${fileData.title}, Filename=${fileData.filename}, FileContent=${fileData.fileContent.size} bytes")
        }
        val fileParts = mutableListOf<MultipartBody.Part>()

        for (fileData in listaArchivos) {

            val fileRequestBody = fileData.fileContent.toRequestBody("application/octet-stream".toMediaTypeOrNull())
            val filePart = MultipartBody.Part.createFormData("files[]", fileData.filename, fileRequestBody)

            val titlePart = MultipartBody.Part.createFormData("titles[]", fileData.title)
            fileParts.add(filePart)
            fileParts.add(titlePart)
        }

        val call = apiService.uploadFilesUpdated(authHeader, id, fileParts)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("FileUpload", "Archivos enviados actualizados exitosamente")
                } else {
                    Log.e("FileUpload", "Error en la respuesta del servidor: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("FileUpload", "Error en la solicitud: ${t.message}")
            }
        })
    }
    private fun executeMethodUpdate(id: Int, methodPayment: String?) {
        btnSend.isClickable = false
        val jwt = preferences["jwt", ""]
        val authHeader = "Bearer $jwt"
        val spCountry = findViewById<Spinner>(R.id.spinnerPaises)
        val nationality = spCountry.selectedItem.toString()
        val nic_ruc =
            findViewById<EditText>(R.id.etNicRuc).text.toString() // Obtén el valor del EditText
        val locality =
            findViewById<EditText>(R.id.etDir2).text.toString() // Obtén el valor del EditText
        val street_and_number =
            findViewById<EditText>(R.id.etDir1).text.toString() // Obtén el valor del EditText
        val spConPayment = findViewById<Spinner>(R.id.spinnerCondPago)
        val typePayment = spConPayment.selectedItem.toString()

        val selectedPolicies = ArrayList<Policy>()
        for (checkBox in checkBoxList) {
            if (checkBox.isChecked) {
                val id = checkBox.id // Asumiendo que el ID proviene del checkbox
                val title = checkBox.text.toString()
                val content = "" // Debe ser inicializado con un valor válido, según tu modelo
                val isChecked = checkBox.isChecked
                val pivot = PolicyPivot(0, 0, 0) // Debe ser inicializado según tu modelo

                selectedPolicies.add(Policy(id, title, content, isChecked, pivot))
            }
        }
        val requestData = RequestData(selectedPolicies, questionsWithResponses)
        Log.d("UpdateRequestLog", "ID: $id")
        Log.d("UpdateRequestLog", "NIC/RUC: $nic_ruc")
        Log.d("UpdateRequestLog", "NIC/RUC: $nationality")
        Log.d("UpdateRequestLog", "LOCALIDAD: $locality")
        Log.d("UpdateRequestLog", "CALLE/NUMERO: $street_and_number")
        Log.d("UpdateRequestLog", "Type Payment: $typePayment")
        Log.d("UpdateRequestLog", "Method Payment: $methodPayment")
        Log.d("UpdateRequestLog", "Request Data: $requestData")

        val call = apiService.updateRequest(
            authHeader,
            id,
            nationality,
            nic_ruc,
            locality,
            street_and_number,
            typePayment,
            methodPayment,
            requestData
        )
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    sendUpdateFiles(id)
                    val message = "Su solicitud fue actualizada correctamente."
                    val intent = Intent(
                        this@FormRequestSupplierActivity,
                        SuccesfulRegisterActivity::class.java
                    )
                    intent.putExtra(
                        "message",
                        message
                    ) // Configura el mensaje en la intención antes de iniciar la actividad
                    startActivity(intent)
                    finish()
                } else {
                    // Manejo de errores
                    val errorMessage = when (response.code()) {
                        401 -> "No autorizado"
                        404 -> "Proveedor no encontrado"
                        500 -> "Error interno del servidor"
                        else -> "Error desconocido"
                    }
                    toast(errorMessage)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                t.localizedMessage?.let { toast(it) }
            }
        })
    }

    private fun loadCountries(request: SupplierRequest?) {
        val spCountries = findViewById<SearchableSpinner>(R.id.spinnerPaises)

        val call = apiService.getCountries()
        call.enqueue(object : Callback<ArrayList<Countrie>> {
            override fun onResponse(
                call: Call<ArrayList<Countrie>>,
                response: Response<ArrayList<Countrie>>
            ) {
                if (response.isSuccessful) {
                    val countries = response.body() ?: ArrayList()

                    // Create a custom ArrayAdapter with a filter to make it searchable
                    val adapter = object : ArrayAdapter<Countrie>(
                        this@FormRequestSupplierActivity,
                        android.R.layout.simple_list_item_1,
                        countries
                    ) {
                        override fun getFilter(): Filter {
                            return object : Filter() {
                                override fun performFiltering(constraint: CharSequence?): FilterResults {
                                    val filterResults = FilterResults()
                                    val filteredList = ArrayList<Countrie>()

                                    if (constraint.isNullOrBlank()) {
                                        filteredList.addAll(countries)
                                    } else {
                                        val filterPattern = constraint.toString().toLowerCase().trim()

                                        for (country in countries) {
                                            if (country.name.toLowerCase().contains(filterPattern)) {
                                                filteredList.add(country)
                                            }
                                        }
                                    }

                                    filterResults.values = filteredList
                                    filterResults.count = filteredList.size
                                    return filterResults
                                }

                                override fun publishResults(
                                    constraint: CharSequence?,
                                    results: FilterResults?
                                ) {
                                    clear()
                                    addAll(results?.values as ArrayList<Countrie>)
                                    notifyDataSetChanged()
                                }
                            }
                        }
                    }

                    // Set the custom adapter to the SearchableSpinner
                    spCountries.adapter = adapter

                    // Set the listener to handle item selection
                    spCountries.onItemSelectedListener = object :
                        AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parentView: AdapterView<*>?,
                            selectedItemView: View?,
                            position: Int,
                            id: Long
                        ) {
                            // Handle item selection
                        }

                        override fun onNothingSelected(parentView: AdapterView<*>?) {
                            // Do nothing here
                        }
                    }

                    // Set the prompt text
                    spCountries.setTitle("Select a Country")

                    // Handle request and pre-select if necessary
                    if (request != null && request.user.supplier?.nacionality != null) {
                        val nationality = request.user.supplier?.nacionality

                        // Find the position of the nationality in the list of countries
                        val nationalityPosition = adapter.getPosition(
                            countries.find { it.name == nationality }
                        )

                        // If found, select the nationality in the spinner
                        if (nationalityPosition != -1) {
                            spCountries.setSelection(nationalityPosition)
                        }
                    }

                } else {
                    // Handle unsuccessful response here
                }
            }

            override fun onFailure(call: Call<ArrayList<Countrie>>, t: Throwable) {
                // Handle failure in the request here
            }
        })
    }


    private fun loadTypesPayments(request: SupplierRequest?) {
        val spinnerCondPago = findViewById<Spinner>(R.id.spinnerCondPago)

        val call = apiService.getTypesPayments()
        call.enqueue(object : Callback<ArrayList<TypePayment>> {
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

                        // Si hay una solicitud y el tipo de pago está presente, selecciona automáticamente el valor
                        request?.type_payment?.name?.let { selectedTypePayment ->
                            val position = adapter.getPosition(selectedTypePayment)
                            if (position != Spinner.INVALID_POSITION) {
                                spinnerCondPago.setSelection(position)
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<TypePayment>>, t: Throwable) {
                Toast.makeText(
                    this@FormRequestSupplierActivity,
                    "Ocurrió un problema al cargar los tipos de pago del formulario",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        })
    }

    private fun loadMethodsPayments(request: SupplierRequest?) {
        val rgMethodsPayments =
            findViewById<RadioGroup>(R.id.rgTipoPago) // Referencia al RadioGroup
        val jwt = preferences["jwt", ""]
        val call = apiService.getMethodsPayments("Bearer $jwt")
        call.enqueue(object : Callback<ArrayList<MethodPayment>> {

            override fun onFailure(call: Call<ArrayList<MethodPayment>>, t: Throwable) {
                Toast.makeText(
                    this@FormRequestSupplierActivity,
                    "Ocurrió un problema al cargar los tipos de pago del formulario",
                    Toast.LENGTH_SHORT
                ).show()
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
                        for (methodPayment in methodsPayments) {
                            val radioButton = RadioButton(this@FormRequestSupplierActivity)
                            radioButton.text = methodPayment.name
                            radioButton.id = View.generateViewId() // Asigna un ID único
                            // Obtiene el valor del padding desde dimens.xml
                            val paddingInPx =
                                resources.getDimension(R.dimen.radio_button_padding).toInt()

                            // Aplica el padding a los RadioButtons
                            radioButton.setPadding(
                                paddingInPx,
                                paddingInPx,
                                paddingInPx,
                                paddingInPx
                            )

                            val params = RadioGroup.LayoutParams(
                                RadioGroup.LayoutParams.WRAP_CONTENT,
                                RadioGroup.LayoutParams.WRAP_CONTENT
                            )
                            params.weight = 1f
                            params.width = 0
                            radioButton.layoutParams = params
                            rgMethodsPayments.addView(radioButton)

                            // Si hay una solicitud y el método de pago está presente, selecciona automáticamente el valor
                            request?.method_payment?.name?.let { selectedMethodPayment ->
                                if (selectedMethodPayment == methodPayment.name) {
                                    radioButton.isChecked = true
                                }
                            }
                        }

                        // Establece un listener para gestionar la selección
                        rgMethodsPayments.setOnCheckedChangeListener { group, checkedId ->
                            val selectedRadioButton = findViewById<RadioButton>(checkedId)
                            methodPayment = selectedRadioButton.text.toString()
                            // Hacer algo con el método de pago seleccionado
                        }
                    }
                }
            }
        })
    }

    private fun loadPolicies(request: SupplierRequest?) {
        val call = apiService.getPolicies()
        call.enqueue(object : Callback<ArrayList<Policy>> {
            override fun onResponse(
                call: Call<ArrayList<Policy>>,
                response: Response<ArrayList<Policy>>
            ) {
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

                            // Si hay una solicitud y la política está presente, selecciona automáticamente el valor
                            request?.policies?.let { selectedPolicies ->
                                if (selectedPolicies.any { it.id == policy.id }) {
                                    checkBox.isChecked = true
                                }
                            }

                            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                                val policyId = buttonView.id
                                // Realiza acciones según sea necesario
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

    private fun loadQuestions(request: SupplierRequest?) {
        val call = apiService.getQuestions()
        call.enqueue(object : Callback<ArrayList<Question>> {
            override fun onResponse(
                call: Call<ArrayList<Question>>,
                response: Response<ArrayList<Question>>
            ) {
                if (response.isSuccessful) {
                    val questions = response.body()

                    if (questions != null) {
                        for (question in questions) {
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
                            preguntaTextView.text = question.question

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
                            siRadioButton.id = View.generateViewId()
                            // Asignar un ID único a siRadioButton
                            val noRadioButton = RadioButton(this@FormRequestSupplierActivity)
                            noRadioButton.layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                            noRadioButton.text = "No"
                            noRadioButton.id = View.generateViewId()
                            // Asignar un ID único a noRadioButton
                            // Si hay una solicitud y la pregunta está presente, selecciona automáticamente el valor
                            request?.questions?.let { selectedQuestions ->
                                val selectedQuestion =
                                    selectedQuestions.find { it.id == question.id }
                                if (selectedQuestion != null) {
                                    when (selectedQuestion.pivot.response) {
                                        1 -> {
                                            siRadioButton.isChecked = true
                                            // Update questionWithResponse for "Si" response
                                            val preguntaId = question.id
                                            val questionWithResponse =
                                                QuestionResponse(preguntaId, true)
                                            updateQuestionResponse(questionWithResponse)
                                        }

                                        0 -> {
                                            noRadioButton.isChecked = true
                                            // Update questionWithResponse for "No" response
                                            val preguntaId = question.id
                                            val questionWithResponse =
                                                QuestionResponse(preguntaId, false)
                                            updateQuestionResponse(questionWithResponse)
                                        }
                                    }
                                }
                            }

                            // Agregar OnCheckedChangeListener a siRadioButton
                            siRadioButton.setOnCheckedChangeListener { _, isChecked ->
                                if (isChecked) {
                                    val preguntaId = question.id
                                    val questionWithResponse = QuestionResponse(preguntaId, true)
                                    updateQuestionResponse(questionWithResponse)
                                }
                            }

                            // Agregar OnCheckedChangeListener a noRadioButton
                            noRadioButton.setOnCheckedChangeListener { _, isChecked ->
                                if (isChecked) {
                                    val preguntaId = question.id
                                    val questionWithResponse = QuestionResponse(preguntaId, false)
                                    updateQuestionResponse(questionWithResponse)
                                }
                            }

                            // Agrega los RadioButtons al RadioGroup
                            radioGroup.addView(siRadioButton)
                            radioGroup.addView(noRadioButton)

                            questionLayout.addView(preguntaTextView)
                            questionLayout.addView(radioGroup)

                            val questionContainer =
                                findViewById<LinearLayout>(R.id.questionContainer)
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

    private fun updateQuestionResponse(questionWithResponse: QuestionResponse) {
        // Actualizar o agregar la respuesta en la lista según la lógica de tu aplicación
        val existingResponseIndex =
            questionsWithResponses.indexOfFirst { it.preguntaId == questionWithResponse.preguntaId }
        if (existingResponseIndex != -1) {
            // Si ya hay una respuesta para esta pregunta, actualizarla
            questionsWithResponses[existingResponseIndex] = questionWithResponse
        } else {
            // Si no hay una respuesta para esta pregunta, agregarla
            questionsWithResponses.add(questionWithResponse)
        }
    }
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        val steps = arrayOf(R.id.llFormStep1, R.id.llFormStep2, R.id.llFormStep3, R.id.llFormStep4, R.id.llFormStep5)

        when {
            currentStep > 1 -> {
                findViewById<LinearLayout>(steps[currentStep - 1]).visibility = View.GONE
                findViewById<LinearLayout>(steps[currentStep - 2]).visibility = View.VISIBLE
                currentStep--
            }

            currentStep == 1 -> showExitConfirmationDialog()
        }

        updateProgressBar()
    }

    private fun showExitConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("¿Estás seguro de que quieres salir?")
            .setMessage("Si abandonas el registro, los datos que ingresaste se perderán")
            .setPositiveButton("Sí, salir") { _, _ -> finish() }
            .setNegativeButton("Continuar con el registro") { dialog, _ -> dialog.dismiss() }

        val dialog = builder.create()
        dialog.show()
    }

    private fun updateProgressBar() {
        val progress = currentStep * 20
        progressBar.progress = progress
    }

    //     Función para validar el formulario
    private fun validateForm(): Boolean {

        val dir1 = findViewById<EditText>(R.id.etDir1).text.toString()
        val dir2 = findViewById<EditText>(R.id.etDir2).text.toString()
        val rgTipoPago = findViewById<RadioGroup>(R.id.rgTipoPago)

        val selectedTypePaymentId = rgTipoPago.checkedRadioButtonId
        if (dir2.isEmpty() || !isValidInput(dir2)) {
            etDir2.error = "Ingrese su localidad donde reside o estado sin símbolos extraños"
            return false
        }

        if (dir1.isEmpty() || !isValidInput(dir1)) {
            etDir1.error = "Ingrese la calle o numero de residencia sin símbolos extraños"
            return false
        }

        if (selectedTypePaymentId == -1) {
            // Ningún RadioButton seleccionado en el grupo de Tipo de Pago
            toast("Debes seleccionar al menos un tipo de pago")
            return false
        }

        return true  // El formulario es válido
    }
    private fun isValidInput(input: String): Boolean {
        val regex = Regex("[a-zA-Z0-9 ]+") // Permitir letras, números y espacios
        return regex.matches(input)
    }
}