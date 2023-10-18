package iberoplast.pe.gespro.model

data class Supplier (
    val id: Int,
    val nacionality: String,
    val nic_ruc: String,
    val id_user: Int,
    val state: String,

    val user: User,

    )