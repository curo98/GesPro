package iberoplast.pe.gespro.io.response

import Role
import iberoplast.pe.gespro.model.User

data class LoginResponse (
    val success: Boolean,
    val user: User,
    val role: Role,
    val jwt: String
)