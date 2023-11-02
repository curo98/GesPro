package iberoplast.pe.gespro.model

data class SupplierRequest(
    val id: Int,
    val id_user: Int,
    val id_type_payment: Int,
    val id_method_payment: Int,
    val user: User,
    val type_payment: TypePayment,
    val method_payment: MethodPayment,
    val stateTransitions: List<StateTransition>
){
    fun getFinalState(): StateRequest? {
        // Verificar si hay al menos una transición de estado
        if (stateTransitions.isNotEmpty()) {
            // Obtener la última transición de estado
            val lastTransition = stateTransitions.last()
            // Verificar si existe un "to_state"
            if (lastTransition.toState != null) {
                return lastTransition.toState
            }
            // Si no hay "to_state", mostrar "from_state"
            return lastTransition.fromState
        }
        return null // No hay transiciones de estado
    }
}