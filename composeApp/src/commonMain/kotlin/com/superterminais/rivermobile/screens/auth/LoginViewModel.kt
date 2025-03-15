package com.example.rivermobile.auth

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.network.HttpException
import com.superterminais.rivermobile.data.RiverPortHubRepository
import com.superterminais.rivermobile.data.auth.LoginRequest
import com.superterminais.rivermobile.data.permission.Permissions
import com.superterminais.rivermobile.data.user.UserSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    data object Idle : AuthState()
    data object Loading : AuthState()
    data object Success : AuthState()
    data class Error(val message: String) : AuthState() {
        override fun toString(): String = "Error: $message"
    }
}

class LoginViewModel(private val repository: RiverPortHubRepository) : ViewModel() {

    private val _authState: MutableStateFlow<AuthState> = MutableStateFlow(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    val loginEnabled by derivedStateOf { email.isNotEmpty() && password.isNotEmpty() }

    fun onEmailChange(email: String) {
        this.email = email
    }

    fun onPasswordChange(password: String) {
        this.password = password
    }

    fun login() {
        val loginRequest = LoginRequest(email, password)

        viewModelScope.launch {
            try {
                _authState.value = AuthState.Loading

                val response = repository.fetchUserData(loginRequest = loginRequest)

                if (!response.isUserInternal()) {
                    _authState.value = AuthState.Error(
                        message = "Usuário externo não permitido",
                    )
                    return@launch
                }

                val permissions = response.getPermissions()
                val mainPermission = permissions.firstOrNull {
                    it == Permissions.APP_NEG_DIRETOR.value || it == Permissions.APP_NEG_GERENCIA.value || it == Permissions.APP_NEG_COMERCIAL.value
                }

                if (mainPermission == null) {
                    _authState.value = AuthState.Error(
                        message = "Usuário sem permissão para acessar o aplicativo",

                        )
                    return@launch
                }

                response.let {
                    UserSession.initialize(
                        id = it.id,
                        token = it.token,
                        name = it.name,
                        permissions = it.getPermissions(),
                        mainPermission = mainPermission
                    )
                }

                _authState.value = AuthState.Success
            } catch (e: HttpException) {
                _authState.value = AuthState.Error(message = "Credenciais inválidas")
            } catch (e: Exception) {
                _authState.value = AuthState.Error(
                    message = "Erro ao realizar login: ${e.message}",
                )
            }
        }
    }
}