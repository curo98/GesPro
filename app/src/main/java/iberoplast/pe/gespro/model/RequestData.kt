package iberoplast.pe.gespro.model

data class RequestData(
    val selectedPolicies: List<Policy>,
    val questionResponses: List<QuestionResponse>
)