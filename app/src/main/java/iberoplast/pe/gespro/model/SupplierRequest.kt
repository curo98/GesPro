package iberoplast.pe.gespro.model

data class SupplierRequest(
    val id: Int,
    val id_user: Int,
    val id_state: Int,
    val id_type_payment: Int,
    val id_method_payment: Int,
    val user: User,
    val state: StateRequest,
    val type_payment: TypePayment,
    val method_payment: MethodPayment,
)