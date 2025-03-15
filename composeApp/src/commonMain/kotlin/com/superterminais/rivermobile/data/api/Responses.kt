package com.superterminais.rivermobile.data.api

import kotlinx.serialization.Serializable

@Serializable
data class Pagination(
    val total_itens: Double,
    val pages: Double,
    val page_itens: Int,
)

@Serializable
data class PaginatedResponse<T>(
    val dataset: ArrayList<T>,
    val pagination: Pagination,
)

@Serializable
data class DefaultResponse(
    val message: String,
)