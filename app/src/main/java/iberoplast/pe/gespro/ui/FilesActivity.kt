package iberoplast.pe.gespro.ui

import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import iberoplast.pe.gespro.R
import iberoplast.pe.gespro.io.ApiService
import iberoplast.pe.gespro.model.Documents

class FilesActivity : AppCompatActivity() {
    private val apiService: ApiService by lazy {
        ApiService.create()
    }
    private val listaArchivos = mutableListOf<Documents>()

    private lateinit var getContent1: ActivityResultLauncher<String>
    private lateinit var getContent2: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_files)

        val btnAdjuntar1 = findViewById<Button>(R.id.btnAdjuntar1)
        val tvAdjuntar1 = findViewById<TextView>(R.id.tvAdjuntar1)

        val btnAdjuntar2 = findViewById<Button>(R.id.btnAdjuntar2)
        val tvAdjuntar2 = findViewById<TextView>(R.id.tvAdjuntar2)

//        // Inicializa getContent1 para el botón 1
//        getContent1 = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
//            uri?.let { selectedUri ->
//                val name = getFileName(selectedUri)
//                val uri = getContentBytes(selectedUri)
//                val fileData = Documents(0, name, uri, 0, Supplier(0, "", "", "", "", 0, "", "", "", User(0, "", "", 0, "", null, null)))
//                tvAdjuntar1.text = "Archivo seleccionado: $name"
//                listaArchivos.add(fileData)
//            }
//        }
//
//        // Inicializa getContent2 para el botón 2
//        getContent2 = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
//            uri?.let { selectedUri ->
//                val name = getFileName(selectedUri)
//                val uri = getContentBytes(selectedUri)
//                val fileData = Documents(0, name, uri, 0, Supplier(0, "", "", "", "", 0, "", "", "", User(0, "", "", 0, "", null, null)))
//                tvAdjuntar2.text = "Archivo seleccionado: $name"
//                listaArchivos.add(fileData)
//            }
//        }

        // Configuración de eventos de clic para el botón 1
        btnAdjuntar1.setOnClickListener {
            openGallery(getContent1)
        }

        // Configuración de eventos de clic para el botón 2
        btnAdjuntar2.setOnClickListener {
            openGallery(getContent2)
        }

        val btnLoadFiles = findViewById<Button>(R.id.btnLoadFiles)
        btnLoadFiles.setOnClickListener {
//            sendFiles()
        }
    }

    private fun openGallery(getContent: ActivityResultLauncher<String>) {
        // Lanzar la actividad de selección de archivos
        getContent.launch("*/*")
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
//    private fun sendFiles() {
//
//        for (fileData in listaArchivos) {
//            Log.d("FileUpload", "Archivo a enviar: ${fileData.filename}")
//        }
//        val fileParts = mutableListOf<MultipartBody.Part>()
//
//        for (fileData in listaArchivos) {
//            val fileRequestBody = fileData.uri.toRequestBody("application/octet-stream".toMediaTypeOrNull())
//            val filePart = MultipartBody.Part.createFormData("files[]", fileData.filename, fileRequestBody)
//            fileParts.add(filePart)
//        }
//
//        val call = apiService.uploadFiles(fileParts)
//        call.enqueue(object : Callback<Void> {
//            override fun onResponse(call: Call<Void>, response: Response<Void>) {
//                if (response.isSuccessful) {
//                    Log.d("FileUpload", "Archivos enviados exitosamente")
//                } else {
//                    Log.e("FileUpload", "Error en la respuesta del servidor: ${response.code()}")
//                }
//            }
//
//            override fun onFailure(call: Call<Void>, t: Throwable) {
//                Log.e("FileUpload", "Error en la solicitud: ${t.message}")
//            }
//        })
//    }
}