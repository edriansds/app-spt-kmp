package com.superterminais.rivermobile.data.extension

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StorageExtension(
    @SerialName("id") val id: Int,
    @SerialName("dtCriacaoFormatada") val createdAt: String,
    @SerialName("nmCliente") val clientName: String,
    @SerialName("cnpj") val clientDocument: String,
    @SerialName("tipo") val type: String,
    @SerialName("periodoAtual") val period: String,
    @SerialName("diasSolicitados") val days: Int,
    @SerialName("valorPerda") val lossValue: Double,
    @SerialName("motivo") val reason: String?,
    @SerialName("status") val situation: String,
    @SerialName("statusParaSincronizacao") val statusForSync: String,
    @SerialName("conteineres") val containers: String
) {
    fun getContainers(): List<String> {
        return containers.split(",").map { it.trim() }
    }
}