package com.superterminais.rivermobile.data

import com.superterminais.rivermobile.data.api.DefaultResponse
import com.superterminais.rivermobile.data.api.PaginatedResponse
import com.superterminais.rivermobile.data.auth.LoginDocumentRequest
import com.superterminais.rivermobile.data.auth.LoginRequest
import com.superterminais.rivermobile.data.discount.DiscountData
import com.superterminais.rivermobile.data.discount.DiscountIdRequest
import com.superterminais.rivermobile.data.discount.UpdateDiscountSituationRequest
import com.superterminais.rivermobile.data.extension.StorageExtension
import com.superterminais.rivermobile.data.extension.StorageExtensionFilters
import com.superterminais.rivermobile.data.extension.StorageExtensionUpdateRequest
import com.superterminais.rivermobile.data.profile.ProfileData
import com.superterminais.rivermobile.data.user.UserData
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody

interface RiverPortHubApi {
    suspend fun resetToken(authorization: String): UserData
    suspend fun fetchUserData(loginRequest: LoginRequest): UserData
    suspend fun fetchUserDataByDocument(loginDocumentRequest: LoginDocumentRequest): UserData
    suspend fun fetchProfileDataByToken(): ProfileData
    suspend fun getDiscounts(
        offset: Int = 0,
        limit: Int = 50,
        orderBy: String = "ID",
        orderAsc: Boolean = false,
        id: Int? = null,
        idUsuarioAnalise: Int? = null,
        campoPesquisa: String = "",
        ignoreInterceptor: Boolean = false,
        authorization: String? = null,
        situacao: String = "",
        statusSincronizacao: String = "",
        categoriaAprovacao: String = ""
    ): PaginatedResponse<DiscountData>

    suspend fun updateDiscountSituation(updateDiscountSituationRequest: DiscountIdRequest): DefaultResponse
    suspend fun deleteDiscountForSync(id: Int): DefaultResponse
    suspend fun includeDiscountForSync(updateDiscountSituationRequest: UpdateDiscountSituationRequest): DefaultResponse
    suspend fun getStorageExtensions(
        offset: Int = 0,
        limit: Int = 50,
        orderBy: String = "Id",
        orderAsc: Boolean = false,
        campoPesquisa: String = "",
        ignoreInterceptor: Boolean = false,
        authorization: String? = null,
        statusSincronizacao: String = "",
        filters: StorageExtensionFilters = StorageExtensionFilters()
    ): PaginatedResponse<StorageExtension>

    suspend fun includeStorageExtensionForSync(updateDiscountSituationRequest: StorageExtensionUpdateRequest): DefaultResponse
    suspend fun deleteStorageExtensionForSync(id: Int): DefaultResponse
    suspend fun updateStorageExtensionSituation(updateDiscountSituationRequest: StorageExtensionUpdateRequest): DefaultResponse
}

class KtorRiverPortHubApi(private val client: HttpClient) : RiverPortHubApi {
    companion object {
        private const val API_URL = "https://192.168.0.134:8443/SuperTerminaisAPI-qa/api/"

        private const val USERS_URL = "users/"
        private const val DISCOUNTS_URL = "faturamento/negociacao/"
        private const val STORAGE_EXTENSIONS_URL = "faturamento/extensao-armazenagem"
    }

    override suspend fun resetToken(authorization: String): UserData {
        return client.get("$API_URL${USERS_URL}reset-token") {
            header("Authorization", authorization)
        }.body<UserData>()
    }

    override suspend fun fetchUserData(loginRequest: LoginRequest): UserData {
        return client.post("$API_URL${USERS_URL}login") {
            setBody(loginRequest)
        }.body<UserData>()
    }

    override suspend fun fetchUserDataByDocument(loginDocumentRequest: LoginDocumentRequest): UserData {
        return client.post("$API_URL${USERS_URL}login-cpf") {
            setBody(loginDocumentRequest)
        }.body<UserData>()
    }

    override suspend fun fetchProfileDataByToken(): ProfileData {
        return client.get("$API_URL${USERS_URL}userByToken").body<ProfileData>()
    }

    override suspend fun getDiscounts(
        offset: Int,
        limit: Int,
        orderBy: String,
        orderAsc: Boolean,
        id: Int?,
        idUsuarioAnalise: Int?,
        campoPesquisa: String,
        ignoreInterceptor: Boolean,
        authorization: String?,
        situacao: String,
        statusSincronizacao: String,
        categoriaAprovacao: String
    ): PaginatedResponse<DiscountData> {
        return client.get("$API_URL${DISCOUNTS_URL}buscar-negociacoes") {
            parameter("offset", offset)
            parameter("limit", limit)
            parameter("orderBy", orderBy)
            parameter("orderAsc", orderAsc)
            id?.let { parameter("id", it) }
            idUsuarioAnalise?.let { parameter("idUsuarioAnalise", it) }
            if (ignoreInterceptor) header("Ignore-Interceptor", ignoreInterceptor.toString())
            if (authorization != null) header("Authorization", authorization)
            if (campoPesquisa.isNotEmpty()) parameter("campoPesquisa", campoPesquisa)
            if (situacao.isNotEmpty()) parameter("situacao", situacao)
            if (statusSincronizacao.isNotEmpty()) parameter(
                "statusSincronizacao", statusSincronizacao
            )
            if (categoriaAprovacao.isNotEmpty()) parameter("categoriaAprovacao", categoriaAprovacao)
        }.body<PaginatedResponse<DiscountData>>()
    }

    override suspend fun updateDiscountSituation(updateDiscountSituationRequest: DiscountIdRequest): DefaultResponse {
        return client.patch("$API_URL${DISCOUNTS_URL}atualizar-situacao-negociacao") {
            setBody(updateDiscountSituationRequest)
        }.body<DefaultResponse>()
    }

    override suspend fun deleteDiscountForSync(id: Int): DefaultResponse {
        return client.delete("$API_URL${DISCOUNTS_URL}excluir-negociacao-para-sincronizacao/$id")
            .body()
    }

    override suspend fun includeDiscountForSync(updateDiscountSituationRequest: UpdateDiscountSituationRequest): DefaultResponse {
        return client.post("$API_URL${DISCOUNTS_URL}incluir-negociacao-para-sincronizacao") {
            setBody(updateDiscountSituationRequest)
        }.body<DefaultResponse>()
    }

    override suspend fun getStorageExtensions(
        offset: Int,
        limit: Int,
        orderBy: String,
        orderAsc: Boolean,
        campoPesquisa: String,
        ignoreInterceptor: Boolean,
        authorization: String?,
        statusSincronizacao: String,
        filters: StorageExtensionFilters
    ): PaginatedResponse<StorageExtension> {
        return client.post("$API_URL${STORAGE_EXTENSIONS_URL}") {
            parameter("offset", offset)
            parameter("limit", limit)
            parameter("orderBy", orderBy)
            parameter("orderAsc", orderAsc)
            if (ignoreInterceptor) header("Ignore-Interceptor", ignoreInterceptor.toString())
            if (authorization != null) header("Authorization", authorization)
            if (campoPesquisa.isNotEmpty()) parameter("campoPesquisa", campoPesquisa)
            if (statusSincronizacao.isNotEmpty()) parameter(
                "statusSincronizacao", statusSincronizacao
            )
            setBody(filters)
        }.body()
    }

    override suspend fun includeStorageExtensionForSync(updateDiscountSituationRequest: StorageExtensionUpdateRequest): DefaultResponse {
        return client.post("$API_URL${STORAGE_EXTENSIONS_URL}/incluir-extensao-para-sincronizacao") {
            setBody(updateDiscountSituationRequest)
        }.body()
    }

    override suspend fun deleteStorageExtensionForSync(id: Int): DefaultResponse {
        return client.delete("$API_URL${STORAGE_EXTENSIONS_URL}/excluir-sincronizacao-extensao/$id")
            .body()
    }

    override suspend fun updateStorageExtensionSituation(updateDiscountSituationRequest: StorageExtensionUpdateRequest): DefaultResponse {
        return client.patch("$API_URL${STORAGE_EXTENSIONS_URL}/atualizar-status-extensao") {
            setBody(updateDiscountSituationRequest)
        }.body()
    }
}