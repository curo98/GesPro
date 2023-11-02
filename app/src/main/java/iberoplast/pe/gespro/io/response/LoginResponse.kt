package iberoplast.pe.gespro.io.response

import iberoplast.pe.gespro.model.User

data class LoginResponse (
    val success: Boolean,
    val user: User,
    val jwt: String
)