package com.superterminais.rivermobile.screens.extension.details

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.superterminais.rivermobile.components.ButtonAction
import com.superterminais.rivermobile.components.DangerButtonColors
import com.superterminais.rivermobile.components.PrimaryButtonColors
import com.superterminais.rivermobile.data.RiverPortHubRepository
import com.superterminais.rivermobile.data.extension.StorageExtension
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StorageExtensionDetailsViewModel(private val repository: RiverPortHubRepository) :
    ViewModel() {

    sealed class UpdateResult {
        data class Success(val message: String) : UpdateResult()
        data class Error(val message: String) : UpdateResult()
    }

    sealed class UiState {
        data object Loading : UiState()
        data class Content(
            val data: StorageExtension,
            val updateResult: UpdateResult? = null
        ) : UiState()

        data class Error(val message: String) : UiState()
    }

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _availableActions: MutableStateFlow<List<ButtonAction>> =
        MutableStateFlow(emptyList())
    val availableActions: StateFlow<List<ButtonAction>> = _availableActions.asStateFlow()

    private fun setAvailableActionsForUser() {
        val actions = mutableListOf<ButtonAction>()
        val extensionData = (uiState.value as UiState.Content).data

        val updateAllowed = extensionData.situation == "PENDENTE"

        if (updateAllowed) {
            actions.add(
                ButtonAction(
                    "APROVADO",
                    "Aprovar",
                    icon = Icons.Filled.Check,
                    color = PrimaryButtonColors
                )
            )
            actions.add(
                ButtonAction(
                    "REPROVADO",
                    "Reprovar",
                    icon = Icons.Filled.Close,
                    color = DangerButtonColors
                )
            )
        }

        _availableActions.value = actions
    }

    fun fetchStorageExtensionData(id: Int) {
        viewModelScope.launch {
            try {
                val extension = repository.getStorageExtension(id)

                _uiState.update {
                    UiState.Content(extension)
                }
                setAvailableActionsForUser()
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "An error occurred")
            }
        }
    }

    fun includeStorageExtensionForSync(id: Int, status: String) {
        viewModelScope.launch {
            try {
                val response = repository.includeStorageExtensionForSync(id, status)

                _uiState.update {
                    UiState.Content(
                        data = (uiState.value as UiState.Content).data,
                        updateResult = UpdateResult.Success(response.message)
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    return@update UiState.Error(e.message ?: "An error occurred")
                }
            }
        }
    }
}