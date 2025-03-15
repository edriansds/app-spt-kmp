package com.superterminais.rivermobile.data.profile

import kotlinx.serialization.Serializable

@Serializable
data class ProfileRole(val id: Int, val roleName: String)

@Serializable
data class ProfileData(
    val name: String,
    val email: String,
    val cpf: String,
    val profilePicture: String?,
    val roles: ArrayList<ProfileRole>
)