package com.superterminais.rivermobile.data.extension

import kotlinx.serialization.Serializable

@Serializable
data class StorageExtensionFilters(
    val id: Int? = null,
    val status: String = "",
    val categoriaAprovacao: String? = null,
    val idUsuarioSincronizacao: Long = 0
)

@Serializable
data class StorageExtensionUpdateRequest(
    val id: Int,
    val status: String = ""
)