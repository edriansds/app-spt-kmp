package com.superterminais.rivermobile.data.user

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class UserData(
    val id: Int,
    val email: String,
    val name: String,
    val token: String,
    val external: Int,
    val permissions: String
) {
    fun isUserInternal(): Boolean {
        return this.external == 0
    }

    fun getPermissions(): List<String> {
        return Json.decodeFromString<Array<UserPermission>>(permissions).map { it.permission }
    }
}

@Serializable
data class UserPermission(
    val permission: String
)