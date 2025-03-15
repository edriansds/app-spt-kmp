package com.superterminais.rivermobile.screens.synchronization

sealed interface ISyncViewModel {
    fun getDataPendingForSync()
    fun removeDataFromSync(id: Int, returnAction: () -> Unit)
}