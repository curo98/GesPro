package iberoplast.pe.gespro.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.fragment.app.Fragment
import iberoplast.pe.gespro.R
import iberoplast.pe.gespro.io.PdfFragmentListener

class PdfFragment : Fragment() {

    private var pdfFragmentListener: PdfFragmentListener? = null

    companion object {
        private const val PDF_URL_KEY = "pdfUrl"

        fun newInstance(pdfUrl: String): PdfFragment {
            val fragment = PdfFragment()
            val args = Bundle()
            args.putString(PDF_URL_KEY, pdfUrl)
            fragment.arguments = args
            return fragment
        }
    }
    // Método para establecer el listener desde la actividad
    fun setPdfFragmentListener(listener: PdfFragmentListener) {
        this.pdfFragmentListener = listener
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pdf_viewer, container, false)
        val pdfWebView: WebView = view.findViewById(R.id.pdfWebView)
        val closeButton: Button = view.findViewById(R.id.closeButton)

        // Configuración del WebView
        val pdfUrl = arguments?.getString(PDF_URL_KEY, "") ?: ""
        pdfWebView.settings.javaScriptEnabled = true
        pdfWebView.settings.setSupportZoom(true)
        pdfWebView.settings.builtInZoomControls = true
        pdfWebView.settings.displayZoomControls = false
        pdfWebView.settings.loadWithOverviewMode = true
        pdfWebView.settings.useWideViewPort = true

        pdfWebView.webChromeClient = WebChromeClient()
        pdfWebView.webViewClient = WebViewClient()
        pdfWebView.loadUrl("https://docs.google.com/gview?embedded=true&url=$pdfUrl")

        // Configuración del botón de cierre
        closeButton.setOnClickListener {
            pdfFragmentListener?.onCloseButtonClicked()
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
        }

        return view
    }
}