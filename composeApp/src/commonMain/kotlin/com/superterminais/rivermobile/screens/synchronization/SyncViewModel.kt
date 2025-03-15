package com.superterminais.rivermobile.screens.synchronization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.superterminais.rivermobile.data.DataStoreRepository
import com.superterminais.rivermobile.data.RiverPortHubRepository
import com.superterminais.rivermobile.data.user.UserSession
import kotlinx.coroutines.launch

class SyncViewModel(private val repository: RiverPortHubRepository, private val dataStore: DataStoreRepository) : ViewModel() {

    fun syncDiscounts(returnAction: () -> Unit) {
        viewModelScope.launch {
            try {
                repository.getDiscountsForSync(UserSession.getId()).forEach {
                    repository.updateDiscountSituation(it.id)
                }
                dataStore.saveLastSyncDate(SyncType.Discounts)
                returnAction()
            } catch (e: Exception) {
                throw e
            }
        }
    }

    fun syncExtensions(returnAction: () -> Unit) {
        viewModelScope.launch {
            try {
                repository.getStorageExtensionsForSync(
                    UserSession.getId().toLong()
                ).forEach {
                    repository.updateStorageExtensionSituation(
                        it.id
                    )
                }
                dataStore.saveLastSyncDate(SyncType.Extensions)
                returnAction()
            } catch (e: Exception) {
                throw e
            }
        }
    }
}