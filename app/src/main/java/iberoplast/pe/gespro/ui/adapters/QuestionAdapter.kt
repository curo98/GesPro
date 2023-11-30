package iberoplast.pe.gespro.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import iberoplast.pe.gespro.R
import iberoplast.pe.gespro.model.Question

class QuestionAdapter : RecyclerView.Adapter<QuestionAdapter.ViewHolder>() {
    private var questions = ArrayList<Question>()
    private var filteredList = ArrayList<Question>()
    var selectedQuestion: Question? = null

    fun updateQuestions(newQuestions: List<Question>) {
        questions.clear()
        questions.addAll(newQuestions)
        filterQuestions("")
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterQuestions(query: String) {
        filteredList.clear()

        if (query.isEmpty()) {
            filteredList.addAll(questions)
        } else {
            for (question in questions) {
                if (question.question.toLowerCase().contains(query)) {
                    filteredList.add(question)
                }
            }
        }

        notifyDataSetChanged()
    }

    // Renombrar la función que utiliza el operador de asignación personalizado
    fun setNewSelectedQuestion(question: Question?) {
        selectedQuestion = question
    }

    fun getPosition(question: Question?): Int {
        question?.let {
            for (index in 0 until questions.size) {
                if (questions[index] == question) {
                    return index
                }
            }
        }
        return -1 // Rol no encontrado en la lista
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvQuestionId: TextView = itemView.findViewById(R.id.tvQuestionId)
        private val tvQuestion: TextView = itemView.findViewById(R.id.tvQuestion)

        fun bind(question: Question) {
            tvQuestionId.text = question.id.toString()
            tvQuestion.text = question.question
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_question, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val question = filteredList[position]

        holder.bind(question)
        // Agregar OnLongClickListener a la vista raíz del elemento
        holder.itemView.setOnLongClickListener { view ->
            setNewSelectedQuestion(question)
            // Muestra el menú contextual
            view.showContextMenu()
            true
        }
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

}