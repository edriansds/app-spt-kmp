package com.superterminais.rivermobile.data

import com.superterminais.rivermobile.data.api.DefaultResponse
import com.superterminais.rivermobile.data.auth.LoginRequest
import com.superterminais.rivermobile.data.discount.DiscountData
import com.superterminais.rivermobile.data.discount.DiscountIdRequest
import com.superterminais.rivermobile.data.discount.UpdateDiscountSituationRequest
import com.superterminais.rivermobile.data.extension.StorageExtension
import com.superterminais.rivermobile.data.extension.StorageExtensionFilters
import com.superterminais.rivermobile.data.extension.StorageExtensionUpdateRequest
import com.superterminais.rivermobile.data.profile.ProfileData
import com.superterminais.rivermobile.data.user.UserData

class RiverPortHubRepository(
    private val riverPortHubApi: KtorRiverPortHubApi,
) {
    suspend fun fetchUserData(loginRequest: LoginRequest): UserData {
        return riverPortHubApi.fetchUserData(loginRequest)
    }

    suspend fun fetchProfileDataByToken(): ProfileData {
        return riverPortHubApi.fetchProfileDataByToken()
    }

    suspend fun resetToken(token: String): UserData {
        return riverPortHubApi.resetToken(token)
    }

    suspend fun getDiscounts(
        campoPesquisa: String,
        situacao: String,
        statusSincronizacao: String,
        categoriaAprovacao: String,
        orderAsc: Boolean
    ): List<DiscountData> {
        return riverPortHubApi.getDiscounts(
            limit = 50,
            offset = 0,
            campoPesquisa = campoPesquisa,
            situacao = situacao,
            statusSincronizacao = statusSincronizacao,
            categoriaAprovacao = categoriaAprovacao,
            orderAsc = orderAsc
        ).dataset
    }

    suspend fun getDiscountsForSync(idUserSync: Int): List<DiscountData> {
        return riverPortHubApi.getDiscounts(
            statusSincronizacao = "SOLICITADO",
            situacao = "PENDENTE",
            idUsuarioAnalise = idUserSync
        ).dataset
    }

    suspend fun getDiscount(id: Int): DiscountData {
        return riverPortHubApi.getDiscounts(limit = 1, offset = 0, id = id).dataset.first()
    }

    suspend fun includeDiscountForSync(id: Int, status: String): DefaultResponse {
        return riverPortHubApi.includeDiscountForSync(UpdateDiscountSituationRequest(id, status))
    }

    suspend fun deleteDiscountForSync(id: Int): DefaultResponse {
        return riverPortHubApi.deleteDiscountForSync(id)
    }

    suspend fun updateDiscountSituation(id: Int): DefaultResponse {
        return riverPortHubApi.updateDiscountSituation(DiscountIdRequest(id))
    }

    suspend fun getStorageExtensions(
        searchQuery: String, syncStatus: String, approvalCategory: String, status: String
    ): List<StorageExtension> {
        return riverPortHubApi.getStorageExtensions(
            campoPesquisa = searchQuery,
            statusSincronizacao = syncStatus,
            filters = StorageExtensionFilters(
                status = status, categoriaAprovacao = approvalCategory
            )
        ).dataset
    }

    suspend fun getStorageExtensionsForSync(idUserSync: Long): List<StorageExtension> {
        return riverPortHubApi.getStorageExtensions(
            statusSincronizacao = "SOLICITADO",
            filters = StorageExtensionFilters(
                idUsuarioSincronizacao = idUserSync,
                status = "PENDENTE"
            )
        ).dataset
    }

    suspend fun getStorageExtension(id: Int): StorageExtension {
        return riverPortHubApi.getStorageExtensions(
            limit = 1, offset = 0, filters = StorageExtensionFilters(id = id)
        ).dataset.first()
    }

    suspend fun includeStorageExtensionForSync(id: Int, status: String): DefaultResponse {
        return riverPortHubApi.includeStorageExtensionForSync(
            StorageExtensionUpdateRequest(
                id, status
            )
        )
    }

    suspend fun deleteStorageExtensionForSync(id: Int): DefaultResponse {
        return riverPortHubApi.deleteStorageExtensionForSync(id)
    }

    suspend fun updateStorageExtensionSituation(id: Int): DefaultResponse {
        return riverPortHubApi.updateStorageExtensionSituation(
            StorageExtensionUpdateRequest(
                id
            )
        )
    }
}
