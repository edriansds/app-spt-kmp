package com.superterminais.rivermobile.data.discount

import kotlinx.serialization.Serializable

@Serializable
data class UpdateDiscountSituationRequest(
    val id: Int,
    val status_negociacao: String,
)

@Serializable
data class DiscountIdRequest(
    val id: Int,
)
