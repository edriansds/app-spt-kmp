package com.superterminais.rivermobile.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.superterminais.rivermobile.data.RiverPortHubRepository
import com.superterminais.rivermobile.data.profile.ProfileData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: RiverPortHubRepository) : ViewModel() {

    sealed class UiState {
        object Loading : UiState()
        data class Error(val message: String) : UiState()
        data class Content(val profile: ProfileData) : UiState()
    }

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun fetchProfile() {
        viewModelScope.launch {
            try {
                val response = repository.fetchProfileDataByToken()
                _uiState.value = UiState.Content(response)
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Ocorreu um erro ao recuperar os dados do perfil")
            }
        }
    }
}