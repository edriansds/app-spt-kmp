package com.superterminais.rivermobile.screens.extension.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.superterminais.rivermobile.data.extension.StorageExtensionStatus
import com.superterminais.rivermobile.data.RiverPortHubRepository
import com.superterminais.rivermobile.data.extension.StorageExtension
import com.superterminais.rivermobile.data.user.UserSession
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StorageExtensionListViewModel(private val repository: RiverPortHubRepository) : ViewModel() {

    sealed class UiState {
        object Loading : UiState()
        data class Content(val extensions: List<StorageExtension>) : UiState()
        data class Error(val message: String) : UiState()
    }

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    val availableSituationsForFiltering: List<StorageExtensionStatus> =
        StorageExtensionStatus.values

    private val _userQuery = MutableStateFlow("")
    val userQuery: StateFlow<String> = _userQuery.asStateFlow()

    private val _selectedStatus =
        MutableStateFlow<StorageExtensionStatus>(StorageExtensionStatus.Pending)
    val selectedStatus: StateFlow<StorageExtensionStatus?> = _selectedStatus.asStateFlow()

    init {
        initialize()
    }

    @OptIn(FlowPreview::class)
    private fun initialize() {
        viewModelScope.launch {
            userQuery.debounce(1000).collectLatest {
                fetchStorageExtensions()
            }
        }

        viewModelScope.launch {
            selectedStatus.collectLatest {
                fetchStorageExtensions()
            }
        }
    }

    fun fetchStorageExtensions() {
        viewModelScope.launch {
            try {
                val syncStatus = when (_selectedStatus.value.value) {
                    "PENDENTE" -> "PENDENTE"
                    "APROVADO", "REPROVADO" -> "REALIZADA"
                    else -> "PENDENTE"
                }

                val approvalCategory = when (UserSession.getMainPermission()) {
                    "APP_NEG_DIRETOR" -> "DIRETORIA"
                    "APP_NEG_GERENCIA" -> "GERENCIA"
                    "APP_NEG_COMERCIAL" -> "COMERCIAL"
                    else -> "COMERCIAL"
                }

                val response = repository.getStorageExtensions(
                    userQuery.value,
                    syncStatus,
                    approvalCategory,
                    selectedStatus.value!!.value
                )

                _uiState.update {
                    return@update UiState.Content(response)
                }
            } catch (e: Exception) {
                _uiState.update {
                    return@update UiState.Error(e.message ?: "An error occurred")
                }
            }
        }
    }

    fun updateUserQuery(query: String) {
        _userQuery.value = query
    }

    fun toggleSituation(situation: StorageExtensionStatus) {
        _selectedStatus.value = if (_selectedStatus.value == situation) return else situation
    }

    fun includeStorageExtensionForSync(id: Int, status: String) {
        viewModelScope.launch {
            try {
                val response = repository.includeStorageExtensionForSync(id, status)

                fetchStorageExtensions()
            } catch (e: Exception) {
                _uiState.update {
                    return@update UiState.Error(e.message ?: "An error occurred")
                }
            }
        }
    }
}