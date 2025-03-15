package com.superterminais.rivermobile.screens.discount.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.superterminais.rivermobile.data.discount.DiscountSituation
import com.superterminais.rivermobile.data.RiverPortHubRepository
import com.superterminais.rivermobile.data.discount.DiscountData
import com.superterminais.rivermobile.data.user.UserSession
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class DiscountListViewModel(private val repository: RiverPortHubRepository) : ViewModel() {

    sealed class UiState {
        object Loading : UiState()
        data class Content(val discounts: List<DiscountData>) : UiState()
        data class Error(val message: String) : UiState()
    }

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    val availableSituationsForFiltering: List<DiscountSituation> = DiscountSituation.values.toList()

    private val _userQuery = MutableStateFlow("")
    val userQuery: StateFlow<String> = _userQuery.asStateFlow()

    private val _selectedSituation = MutableStateFlow<DiscountSituation>(DiscountSituation.Pending)
    val selectedSituation: StateFlow<DiscountSituation?> = _selectedSituation.asStateFlow()

    init {
        initialize()
    }

    @OptIn(FlowPreview::class)
    private fun initialize() {
        viewModelScope.launch {
            userQuery.debounce(1000).collectLatest {
                fetchDiscounts()
            }
        }

        viewModelScope.launch {
            selectedSituation.collectLatest {
                fetchDiscounts()
            }
        }
    }

    fun fetchDiscounts() {
        viewModelScope.launch {
            try {
                val syncStatus: String
                val orderAsc: Boolean

                when (_selectedSituation.value.value) {
                    "APROVADO", "REPROVADO" -> {
                        syncStatus = "REALIZADA"
                        orderAsc = false
                    }

                    else -> {
                        syncStatus = "PENDENTE"
                        orderAsc = true
                    }
                }

                val approvalCategory = when (UserSession.getMainPermission()) {
                    "APP_NEG_DIRETOR" -> "DIRETORIA"
                    "APP_NEG_GERENCIA" -> "GERENCIA"
                    "APP_NEG_COMERCIAL" -> "COMERCIAL"
                    else -> "COMERCIAL"
                }

                val response = repository.getDiscounts(
                    campoPesquisa = _userQuery.value,
                    situacao = _selectedSituation.value.value,
                    statusSincronizacao = syncStatus,
                    categoriaAprovacao = approvalCategory,
                    orderAsc = orderAsc
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

    fun toggleSituation(situation: DiscountSituation) {
        _selectedSituation.value = if (_selectedSituation.value == situation) return else situation
    }

    fun includeDiscountForSync(id: Int, status: String) {
        viewModelScope.launch {
            try {
                repository.includeDiscountForSync(id, status)

                fetchDiscounts()
            } catch (e: Exception) {
                _uiState.update {
                    return@update UiState.Error(e.message ?: "An error occurred")
                }
            }
        }
    }
}