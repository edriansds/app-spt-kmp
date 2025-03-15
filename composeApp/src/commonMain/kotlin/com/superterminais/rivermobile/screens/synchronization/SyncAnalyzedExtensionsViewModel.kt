package com.superterminais.rivermobile.screens.synchronization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.superterminais.rivermobile.data.DataStoreRepository
import com.superterminais.rivermobile.data.RiverPortHubRepository
import com.superterminais.rivermobile.data.extension.StorageExtension
import com.superterminais.rivermobile.data.user.UserSession
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SyncAnalyzedExtensionsViewModel(
    private val repository: RiverPortHubRepository,
    private val dataStore: DataStoreRepository
) : ViewModel(),
    ISyncViewModel {
    sealed class UiState {
        object Loading : UiState()
        data class Content(
            val storageExtensions: List<StorageExtension>, val lastSyncDate: String? = null
        ) : UiState()

        data class Error(val message: String) : UiState()
    }

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _dataFlow = flow {
        while (true) {
            emit(Unit)
            delay(10000)
        }
    }.onEach {
        getDataPendingForSync()
    }

    init {
        _dataFlow.launchIn(viewModelScope)
    }

    override fun getDataPendingForSync() {
        viewModelScope.launch {
            try {
                repository.getStorageExtensionsForSync(UserSession.getId().toLong()).let {
                    _uiState.value = UiState.Content(
                        storageExtensions = it,
                        lastSyncDate = dataStore.getLastSyncDate(SyncType.Extensions)
                    )
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(
                    message = e.message ?: "Erro desconhecido"
                )
            }
        }
    }

    override fun removeDataFromSync(id: Int, returnAction: () -> Unit) {
        viewModelScope.launch {
            try {
                repository.deleteStorageExtensionForSync(id)
                returnAction()
            } catch (e: Exception) {
                _uiState.value = UiState.Error(
                    message = e.message ?: "Erro desconhecido"
                )
            }
        }
    }
}