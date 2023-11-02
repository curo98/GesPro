package iberoplast.pe.gespro.model

data class StateTransition(
    val from_state_id: Int,
    val to_state_id: Int,
    val id_reviewer: Int,
    val fromState: StateRequest,
    val toState: StateRequest,
    val reviewer: User
)
