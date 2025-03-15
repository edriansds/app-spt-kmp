package com.superterminais.rivermobile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.superterminais.rivermobile.data.DataStoreRepository
import com.superterminais.rivermobile.data.RiverPortHubRepository
import com.superterminais.rivermobile.data.permission.Permissions
import com.superterminais.rivermobile.data.user.UserSession
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainViewModel(
    private val dataStore: DataStoreRepository,
    private val repository: RiverPortHubRepository
) : ViewModel() {
    private fun initializeWorkers() {

    }


    fun onAuthentication() {
        initializeWorkers()
    }

    fun verifyAuthentication() {
        runBlocking {
            dataStore.getUserSession().let {
                val userId = it.first ?: return@let
                val userName = it.second ?: return@let
                val token = it.third ?: return@let

                try {
                    repository.resetToken(token).let { response ->
                        dataStore.saveUserSession(response.token, userId, userName)

                        val permissions = response.getPermissions()
                        val mainPermission = permissions.first { permission ->
                            permission == Permissions.APP_NEG_DIRETOR.value ||
                                    permission == Permissions.APP_NEG_GERENCIA.value ||
                                    permission == Permissions.APP_NEG_COMERCIAL.value
                        }

                        UserSession.initialize(
                            id = userId.toInt(),
                            token = response.token,
                            name = userName,
                            permissions = permissions,
                            mainPermission = mainPermission
                        )
                    }
                } catch (e: Exception) {
                    dataStore.clearUserSession()
                    UserSession.clear()
                    return@let
                }
            }
        }
    }

    fun onLogout(onCompleted: () -> Unit) {
        viewModelScope.launch {
            dataStore.clearUserSession()
            UserSession.clear()
        }
        onCompleted()
    }
}