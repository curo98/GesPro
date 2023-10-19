package iberoplast.pe.gespro.ui.modules.state

import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import iberoplast.pe.gespro.R

class StateRequestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_state_request)

        val progressBar: ProgressBar = findViewById(R.id.progressBar4)
        val progressText: TextView = findViewById(R.id.progressText)

// Establecer el progreso del ProgressBar (de 0 a 100)

// Establecer el progreso del ProgressBar (de 0 a 100)
        val progress = 100 // Cambia esto al valor que desees

        progressBar.progress = progress

// Actualizar el valor del TextView para reflejar el progreso

// Actualizar el valor del TextView para reflejar el progreso
        progressText.text = "$progress%"

    }
}