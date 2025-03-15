package com.superterminais.rivermobile.data.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
) {
    val finalizarSessaoAtiva: Boolean = true
}

@Serializable
data class LoginDocumentRequest(
    val cpf: String,
) {
    val token: String = "c6ee6d3e991a556a198c43e14510cd9d"
}
