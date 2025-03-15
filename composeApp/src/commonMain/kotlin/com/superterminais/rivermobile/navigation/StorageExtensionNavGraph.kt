package com.superterminais.rivermobile.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.toRoute
import com.superterminais.rivermobile.screens.extension.list.StorageExtensionListScreen
import com.superterminais.rivermobile.components.CommonAlertDialog
import com.superterminais.rivermobile.screens.extension.details.StorageExtensionDetailsScreen
import kotlinx.serialization.Serializable


@Serializable
object StorageExtensionListNav

@Serializable
data class StorageExtensionDetailsNav(val storageExtensionId: Int)

@Serializable
data class ExtensionInfoDialog(
    val title: String = "Aviso",
    val message: String,
    val isError: Boolean = false,
)

fun NavGraphBuilder.storageExtensionNavGraph(
    navController: NavHostController,
    changeTitle: (String) -> Unit,
    showReturnIcon: (Boolean) -> Unit
) {
    composable<StorageExtensionListNav> {
        changeTitle("Extensões")
        showReturnIcon(false)

        StorageExtensionListScreen() {
            navController.navigate(StorageExtensionDetailsNav(it)) {
                launchSingleTop = true
            }
        }
    }
    composable<StorageExtensionDetailsNav> {
        val args = it.toRoute<StorageExtensionDetailsNav>()

        changeTitle("Detalhes da extensão")
        showReturnIcon(true)
        StorageExtensionDetailsScreen(extensionId = args.storageExtensionId,
            openDialog = { title, message ->
                navController.navigate(
                    ExtensionInfoDialog(
                        title = title,
                        message = message,
                    )
                )
            })
    }
    dialog<ExtensionInfoDialog> {
        val args = it.toRoute<ExtensionInfoDialog>()
        CommonAlertDialog(
            onDismiss = {
                navController.navigate(StorageExtensionListNav) {
                    launchSingleTop = true
                }
            }, title = args.title, text = args.message, isError = args.isError
        )
    }
}