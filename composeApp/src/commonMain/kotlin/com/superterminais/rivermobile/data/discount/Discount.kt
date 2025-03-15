package com.superterminais.rivermobile.data.discount

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DiscountData(
    @SerialName("id") val id: Int,
    @SerialName("nome") val clientName: String,
    @SerialName("cnpj") val clientDocument: String,
    @SerialName("status_negociacao") var situation: String,
    @SerialName("valor") val value: Double,
    @SerialName("desconto_porcentagem") val discountPercentage: Double,
    @SerialName("descricao_tipo_desconto") val discountTypeDescription: String?,
    @SerialName("motivo") val reason: String?,
    @SerialName("dt_cadastro") val creadetAt: String,
    @SerialName("status_sincronizacao") val syncStatus: String,
    @SerialName("categoria_aprovacao") val approvalCategory: String,
    @SerialName("statusParaSincronizacao") val statusForSync: String?,
)