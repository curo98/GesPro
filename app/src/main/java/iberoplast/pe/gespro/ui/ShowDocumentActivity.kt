package iberoplast.pe.gespro.ui

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github.barteksc.pdfviewer.PDFView
import iberoplast.pe.gespro.R

class ShowDocumentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_document)


        // Obtener la URL del PDF desde el Intent
        val url = intent.getStringExtra("url")
        Log.d("ShowDocumentActivityFile", "PDF URL: $url")
        // Obtener la referencia al PDFView desde el layout
        val pdfView: PDFView = findViewById(R.id.pdfView)

        // Cargar y mostrar el PDF desde la URL
        pdfView.fromUri(Uri.parse(url))
            .defaultPage(0) // Página por defecto al cargar
            .enableSwipe(true) // Habilitar el deslizamiento horizontal
            .swipeHorizontal(false) // Desplazamiento vertical por defecto
            .load()
    }
}