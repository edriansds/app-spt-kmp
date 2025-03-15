package com.superterminais.rivermobile.screens.discount.details

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.superterminais.rivermobile.components.ButtonAction
import com.superterminais.rivermobile.components.DangerButtonColors
import com.superterminais.rivermobile.components.PrimaryButtonColors
import com.superterminais.rivermobile.data.RiverPortHubRepository
import com.superterminais.rivermobile.data.discount.DiscountData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DiscountDetailsViewModel(
    private val repository: RiverPortHubRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<DiscountDetailsViewState> =
        MutableStateFlow(DiscountDetailsViewState.Loading)
    val uiState: StateFlow<DiscountDetailsViewState> = _uiState.asStateFlow()

    private val _availableActions: MutableStateFlow<List<ButtonAction>> =
        MutableStateFlow(emptyList())
    val availableActions: StateFlow<List<ButtonAction>> = _availableActions.asStateFlow()

    private fun setAvailableActionsForUser() {
        val actions = mutableListOf<ButtonAction>()
        val discountData = (uiState.value as DiscountDetailsViewState.SuccessContent).discountData

        val updateAllowed = discountData.situation == "PENDENTE"

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

    fun fetchDiscountData(id: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getDiscount(id)

                _uiState.update {
                    DiscountDetailsViewState.SuccessContent(response)
                }

                setAvailableActionsForUser();
            } catch (e: Exception) {
                _uiState.value =
                    DiscountDetailsViewState.Error(e.message ?: "An error occurred")
            }
        }
    }

    fun updateDiscountSituation(id: Int, status: String) {
        viewModelScope.launch {
            try {
                val response = repository.includeDiscountForSync(id, status)

                _uiState.update {
                    DiscountDetailsViewState.SuccessContent(
                        (uiState.value as DiscountDetailsViewState.SuccessContent).discountData,
                        UpdateResult.Success(response.message)
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    DiscountDetailsViewState.Error(e.message ?: "An error occurred")
                }
            }
        }
    }
}

sealed class UpdateResult {
    data class Success(val message: String) : UpdateResult()
    data class Error(val message: String) : UpdateResult()
}

sealed class DiscountDetailsViewState {
    data object Loading : DiscountDetailsViewState()
    data class SuccessContent(
        val discountData: DiscountData, val updateResult: UpdateResult? = null
    ) : DiscountDetailsViewState()

    data class Error(val message: String) : DiscountDetailsViewState() {
        override fun toString(): String = "Error: $message"
    }
}