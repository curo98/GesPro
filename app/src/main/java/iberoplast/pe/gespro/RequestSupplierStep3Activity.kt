package iberoplast.pe.gespro

import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class RequestSupplierStep3Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_supplier_step3)

//        val pdfWebView = findViewById<WebView>(R.id.pdfWebView)
//
//        // URL compartida del archivo PDF en Google Drive
//        val pdfUrl = "https://drive.google.com/file/d/1Y4gwaZnYgfpS2Y2eE6CtMEnMMapuA5aA/view?usp=sharing"
//
//        // Carga el archivo PDF en el WebView
//        pdfWebView.loadUrl(pdfUrl)

        val pdfWebView = findViewById<WebView>(R.id.pdfWebView)

        // Habilitar JavaScript (opcional, dependiendo del contenido del PDF)
        pdfWebView.settings.javaScriptEnabled = true

        // Configurar el cliente WebView
        pdfWebView.webViewClient = WebViewClient()
        pdfWebView.settings.setDomStorageEnabled(true)

        pdfWebView.settings.javaScriptEnabled = true

        // Configurar el escalamiento de la vista del PDF
        pdfWebView.settings.useWideViewPort = true
        pdfWebView.settings.loadWithOverviewMode = true
        pdfWebView.settings.setSupportZoom(true)
        pdfWebView.settings.builtInZoomControls = true
        pdfWebView.settings.displayZoomControls = true


        val pdfUrl = "https://drive.google.com/file/d/1Y4gwaZnYgfpS2Y2eE6CtMEnMMapuA5aA/view?usp=sharing"
        pdfWebView.loadUrl(pdfUrl)



        val btnFormRequest3 = findViewById<Button>(R.id.btnFormRequest3)
        btnFormRequest3.setOnClickListener{
            val intent = Intent(this, RequestSupplierStep4Activity::class.java)
            startActivity(intent)
        }
    }
}