package iberoplast.pe.gespro.ui.modules.request

import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.Gravity
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import iberoplast.pe.gespro.R
import iberoplast.pe.gespro.model.SupplierRequest
import iberoplast.pe.gespro.util.ActionBarUtils
import java.text.SimpleDateFormat
import java.util.Locale

class ShowRequestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_request)

        val btnReturnList = findViewById<Button>(R.id.btnReturnList)

        btnReturnList.setOnClickListener {
            val intent = Intent(this, SupplierRequestsActivity::class.java)
            startActivity(intent)
            finish()
        }

        val llDetailQuestion = findViewById<LinearLayout>(R.id.llDetailQuestion)
        val ibExpandQuestions = findViewById<ImageButton>(R.id.ibExpandQuestions)

        val llDetailPolicies = findViewById<LinearLayout>(R.id.llDetailPolicies)
        val ibExpandPolicies = findViewById<ImageButton>(R.id.ibExpandPolicies)

        val llDetailObservations = findViewById<LinearLayout>(R.id.llDetailObservations)
        val ibExpandObservations = findViewById<ImageButton>(R.id.ibExpandObservations)

        val llDetailTransitions = findViewById<LinearLayout>(R.id.llDetailTransitions)
        val ibExpandTransitions = findViewById<ImageButton>(R.id.ibExpandTransitions)

        val tvQuestion = findViewById<TextView>(R.id.tvQuestion)
        val tvPolicy = findViewById<TextView>(R.id.tvPolicy)
        val tvObservation = findViewById<TextView>(R.id.tvObservation)
        val tvTransition = findViewById<TextView>(R.id.tvTransition)

        val tvName = findViewById<TextView>(R.id.tvName)
        val tvTypePay = findViewById<TextView>(R.id.tvTypePayment)
        val tvMethodPay = findViewById<TextView>(R.id.tvMethodPay)

        val tvCreated_at = findViewById<TextView>(R.id.tvCreated_at)
        val tvUpdated_at = findViewById<TextView>(R.id.tvUpdated_at)

        // val BarProgress and textValue
        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        val textViewProgress = findViewById<TextView>(R.id.text_view_progress)
        val tvLastNameState = findViewById<TextView>(R.id.tvLastNameState)

        val request = intent.getParcelableExtra<SupplierRequest>("request_details")

        if (request != null) {
            ActionBarUtils.setCustomTitle(
                this,
                "Detalle de solicitud con id: ${request.id}"
            )
            val finalState = request.getFinalState()

            if (finalState != null) {
                val progress = when (finalState.name) {
                    "Enviado", "Por recibir" -> 40
                    "Recibida", "Por validar" -> 60
                    "Validada", "Por aprobar" -> 80
                    "Aprobada" -> 100
                    else -> 0 // Otros estados no especificados tendrán un progreso del 0%
                }

                // Configura el ProgressBar y el TextView con el porcentaje
                progressBar.progress = progress
                textViewProgress.text = "$progress%"
                tvLastNameState.text = finalState.name

                // Si el estado es "Rechazada" o "Cancelada", configura el progreso al 100%
                if (finalState.name == "Rechazada" || finalState.name == "Cancelada" || finalState.name == "Desaprobada") {
                    progressBar.progress = 100
                    textViewProgress.text = "100%"
                }
            } else {
                // Maneja el caso en que no hay un estado final
                progressBar.progress = 0
                textViewProgress.text = "0%"
            }

            val questions = request.questions
            val policies = request.policies
            val observations = request.observations
            val transitions = request.stateTransitions

            tvName.text = request.user.name
            tvQuestion.text = "Preguntas: ${questions.size}"
            tvObservation.text = "Observaciones: ${observations.size}"
            tvPolicy.text = "Politicas: ${policies.size}"
            tvTransition.text = "Transiciones: ${transitions.size}"

            // Tu código existente
            val typePaymentText = "Tipo de Pago: \n${request.type_payment.name}"
            applyBoldToTextView(tvTypePay, typePaymentText, "Tipo de Pago:")

            val methodPaymentText = "Método de Pago: \n${request.method_payment.name}"
            applyBoldToTextView(tvMethodPay, methodPaymentText, "Método de Pago:")

            tvCreated_at.text = formatDate(request.created_at)
            tvUpdated_at.text = formatDate(request.updated_at)

            ibExpandQuestions.setOnClickListener {
                val isVisible = llDetailQuestion.visibility == View.VISIBLE

                if (isVisible) {
                    llDetailQuestion.startAnimation(createFadeOutAnimation {
                        llDetailQuestion.visibility = View.GONE
                        ibExpandQuestions.setImageResource(R.drawable.ic_expand_more)
                    })
                } else {
                    llDetailQuestion.visibility = View.VISIBLE
                    llDetailQuestion.startAnimation(createFadeInAnimation())
                    ibExpandQuestions.setImageResource(R.drawable.ic_expand_less)

                    llDetailQuestion.removeAllViews()

                    for (question in questions) {
                        val responseText = if (question.pivot.response == 1) "✓" else "✗"
                        val formattedQuestion = "${question.question}: $responseText"

                        val textView = TextView(this)
                        textView.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        textView.text = formattedQuestion
                        textView.gravity = Gravity.CENTER_HORIZONTAL

                        llDetailQuestion.addView(textView)
                    }
                }
            }

            ibExpandPolicies.setOnClickListener {
                val isVisible = llDetailPolicies.visibility == View.VISIBLE

                if (isVisible) {
                    llDetailPolicies.startAnimation(createFadeOutAnimation {
                        llDetailPolicies.visibility = View.GONE
                        ibExpandPolicies.setImageResource(R.drawable.ic_expand_more)
                    })
                } else {
                    llDetailPolicies.visibility = View.VISIBLE
                    llDetailPolicies.startAnimation(createFadeInAnimation())
                    ibExpandPolicies.setImageResource(R.drawable.ic_expand_less)

                    llDetailPolicies.removeAllViews()

                    for (policie in policies) {
                        val responseText = if (policie.pivot.accepted == 1) "✓" else "✗"
                        val formattedPolicy = "${policie.title}: $responseText"

                        val textView = TextView(this)
                        textView.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        textView.text = formattedPolicy
                        textView.gravity = Gravity.CENTER_HORIZONTAL

                        llDetailPolicies.addView(textView)
                    }
                }
            }

            if (observations.isNotEmpty()){
                val llExpandObservations = findViewById<LinearLayout>(R.id.llExpandObservations)
                llExpandObservations.visibility = View.VISIBLE
                ibExpandObservations.setOnClickListener {
                    val isVisible = llDetailObservations.visibility == View.VISIBLE

                    if (isVisible) {
                        llDetailObservations.startAnimation(createFadeOutAnimation {
                            llDetailObservations.visibility = View.GONE
                            ibExpandObservations.setImageResource(R.drawable.ic_expand_more)
                        })
                    } else {
                        llDetailObservations.visibility = View.VISIBLE
                        llDetailObservations.startAnimation(createFadeInAnimation())
                        ibExpandObservations.setImageResource(R.drawable.ic_expand_less)

                        llDetailObservations.removeAllViews()

                        val observationsCount = observations.size

                        for ((index, observation) in observations.withIndex()) {
                            val created_at = formatDate(observation.created_at)
                            val formattedObservation = "${observation.title} - $created_at"

                            val textView = TextView(this)
                            textView.layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                            textView.text = formattedObservation
                            textView.gravity = Gravity.CENTER_HORIZONTAL

                            val observationText = TextView(this)
                            observationText.layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                            observationText.text = "Observación realizada por: ${observation.user.name}"
                            observationText.gravity = Gravity.CENTER_HORIZONTAL

                            llDetailObservations.addView(textView)
                            llDetailObservations.addView(observationText)

                            // Agregar la línea horizontal si existe un elemento siguiente
                            if (index < observationsCount - 1) {
                                val lineView = View(this)
                                val params = LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    1 // Altura de la línea horizontal
                                )
                                params.setMargins(0, 10.dpToPx(), 0, 10.dpToPx()) // Aplicar padding de 5dp
                                lineView.layoutParams = params
                                lineView.setBackgroundColor(Color.BLACK) // Color de la línea
                                llDetailObservations.addView(lineView)
                            }
                        }
                    }
                }
            }
            ibExpandTransitions.setOnClickListener {
                val isVisible = llDetailTransitions.visibility == View.VISIBLE

                if (isVisible) {
                    llDetailTransitions.startAnimation(createFadeOutAnimation {
                        llDetailTransitions.visibility = View.GONE
                        ibExpandTransitions.setImageResource(R.drawable.ic_expand_more)
                    })
                } else {
                    llDetailTransitions.visibility = View.VISIBLE
                    llDetailTransitions.startAnimation(createFadeInAnimation())
                    ibExpandTransitions.setImageResource(R.drawable.ic_expand_less)

                    llDetailTransitions.removeAllViews() // Limpia las vistas existentes

                    for ((index, transition) in transitions.withIndex()) {
                        // Crear un nuevo LinearLayout para cada elemento de la línea de tiempo
                        val itemLayout = LinearLayout(this)
                        val itemLayoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        itemLayout.orientation = LinearLayout.HORIZONTAL
                        itemLayout.layoutParams = itemLayoutParams

                        // Agregar el TextView con la fecha (centrado verticalmente en la parte superior)
                        val dateTextView = TextView(this)
                        val dateParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        dateTextView.text = formatDate(request.created_at)
                        dateTextView.setTextColor(resources.getColor(R.color.black))
                        dateParams.gravity = Gravity.CENTER
                        itemLayout.addView(dateTextView, dateParams)

                        // Agregar un espacio entre el TextView y la línea vertical
                        val spaceView = View(this)
                        val spaceParams = LinearLayout.LayoutParams(
                            resources.getDimensionPixelSize(R.dimen.space_width),
                            1 // Altura del espacio
                        )
                        itemLayout.addView(spaceView, spaceParams)

                        // Agregar la línea vertical
                        val verticalLine = View(this)
                        val verticalLineParams = LinearLayout.LayoutParams(
                            resources.getDimensionPixelSize(R.dimen.vertical_line_width),
                            LinearLayout.LayoutParams.MATCH_PARENT
                        )
                        verticalLine.setBackgroundColor(resources.getColor(R.color.black))
                        itemLayout.addView(verticalLine, verticalLineParams)

                        // Agregar el icono de check_circle
                        val checkCircle = ImageView(this)
                        val checkCircleParams = LinearLayout.LayoutParams(
                            resources.getDimensionPixelSize(R.dimen.check_circle_size),
                            resources.getDimensionPixelSize(R.dimen.check_circle_size)
                        )
                        checkCircle.setImageResource(R.drawable.ic_check_circle)
                        checkCircleParams.gravity = Gravity.CENTER_VERTICAL // Centra verticalmente el icono
                        checkCircleParams.marginEnd = resources.getDimensionPixelSize(R.dimen.check_circle_margin_end)
                        checkCircleParams.marginStart = resources.getDimensionPixelSize(R.dimen.check_circle_margin_start) // Agregar margen izquierdo
                        itemLayout.addView(checkCircle, checkCircleParams)

                        // Crear un LinearLayout para el contenido
                        val contentLayout = LinearLayout(this)
                        val contentLayoutParams = LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            1f
                        )
                        contentLayout.orientation = LinearLayout.VERTICAL
                        contentLayout.layoutParams = contentLayoutParams

                        // Agregar los TextViews al contenido
                        val fromStateTextView = TextView(this)
                        val fromStateParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        fromStateTextView.text = "De: ${transition.fromState.name}"
                        fromStateTextView.setTextColor(resources.getColor(R.color.black))
                        fromStateTextView.layoutParams = fromStateParams
                        contentLayout.addView(fromStateTextView)

                        val toStateTextView = TextView(this)
                        val toStateParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        toStateTextView.text = "A: ${transition.toState.name}"
                        toStateTextView.setTextColor(resources.getColor(R.color.black))
                        toStateTextView.layoutParams = toStateParams
                        contentLayout.addView(toStateTextView)

                        val reviewerTextView = TextView(this)
                        val reviewerParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        reviewerTextView.text = "Por: ${transition.reviewer.name}"
                        reviewerTextView.setTextColor(resources.getColor(R.color.black))
                        reviewerTextView.layoutParams = reviewerParams
                        contentLayout.addView(reviewerTextView)

                        // Agregar el contenido al elemento de la línea de tiempo
                        itemLayout.addView(contentLayout)

                        // Agregar el elemento de la línea de tiempo al LinearLayout principal
                        llDetailTransitions.addView(itemLayout)

                        // Agregar la línea horizontal después de cada elemento, excepto el último
                        if (index < transitions.size - 1) {
                            val horizontalLine = View(this)
                            val horizontalLineParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                resources.getDimensionPixelSize(R.dimen.horizontal_line_height) // Define la altura de la línea horizontal en tus recursos
                            )
                            horizontalLine.setBackgroundColor(resources.getColor(R.color.black))
                            llDetailTransitions.addView(horizontalLine, horizontalLineParams)
                        }
                    }
                }
            }


        }
    }
    // Función de extensión para convertir dp a píxeles
    fun Int.dpToPx(): Int {
        return (this * Resources.getSystem().displayMetrics.density).toInt()
    }

    private fun createFadeOutAnimation(endAction: () -> Unit): AlphaAnimation {
        val fadeOut = AlphaAnimation(1f, 0f)
        fadeOut.duration = 500
        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                endAction.invoke()
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        return fadeOut
    }

    private fun createFadeInAnimation(): AlphaAnimation {
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.duration = 500
        return fadeIn
    }

    private fun formatDate(dateTime: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yy", Locale.getDefault())

        try {
            val date = inputFormat.parse(dateTime)
            return outputFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            return dateTime // Si ocurre un error, devolvemos el valor original
        }
    }

    // Función para aplicar formato negrita a un TextView
    fun applyBoldToTextView(textView: TextView, text: String, boldText: String) {
        // Crear una instancia de SpannableStringBuilder
        val spannableStringBuilder = SpannableStringBuilder(text)

        // Encontrar la posición del texto que se formateará con negrita
        val startIndex = text.indexOf(boldText)

        // Aplicar estilo negrita solo a la porción específica del texto
        spannableStringBuilder.setSpan(
            StyleSpan(Typeface.BOLD),
            startIndex,
            startIndex + boldText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Asignar el texto formateado al TextView
        textView.text = spannableStringBuilder
    }
}

