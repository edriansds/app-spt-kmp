package com.superterminais.rivermobile.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.toRoute
import com.superterminais.rivermobile.components.CommonAlertDialog
import com.superterminais.rivermobile.screens.synchronization.ISyncViewModel
import com.superterminais.rivermobile.screens.synchronization.SyncAnalyzedDiscountsViewModel
import com.superterminais.rivermobile.screens.synchronization.SyncAnalyzedExtensionsViewModel
import com.superterminais.rivermobile.screens.synchronization.SyncViewModel
import com.superterminais.rivermobile.screens.synchronization.SynchronizationScreen
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel


@Serializable
object SyncDiscountsUpdateNav

@Serializable
data class ManualSyncConfirmationDialog(val type: String)

@Serializable
data class RemovalSyncConfirmationDialog(val id: Int, val type: String)

fun NavGraphBuilder.syncNavGraph(
    navController: NavHostController,
    changeTitle: (String) -> Unit,
    showReturnIcon: (Boolean) -> Unit
) {
    composable<SyncDiscountsUpdateNav> {
        changeTitle("Sincronizar")
        showReturnIcon(false)
        SynchronizationScreen(openManualSyncDialogConfirmation = {
            navController.navigate(ManualSyncConfirmationDialog(it))
        }, openRemoveSyncDialogConfirmation = { id, type ->
            navController.navigate(RemovalSyncConfirmationDialog(id, type))
        })
    }
    dialog<ManualSyncConfirmationDialog> {
        val viewModel = koinViewModel<SyncViewModel>()
        val args = it.toRoute<ManualSyncConfirmationDialog>()

        CommonAlertDialog(
            onDismiss = { navController.popBackStack() },
            dismissText = "Cancelar",
            title = "Confirmação",
            text = "Deseja realizar a sincronização manual?",
            onConfirmation = {
                when (args.type) {
                    "Descontos" -> viewModel.syncDiscounts {
                        navController.navigate(SyncDiscountsUpdateNav) {
                            launchSingleTop = true
                        }
                    }

                    else -> viewModel.syncExtensions {
                        navController.navigate(SyncDiscountsUpdateNav) {
                            launchSingleTop = true
                        }
                    }
                }
            },
        )
    }
    dialog<RemovalSyncConfirmationDialog> {
        val args = it.toRoute<RemovalSyncConfirmationDialog>()
        val viewModel: ISyncViewModel = when (args.type) {
            "Descontos" -> it.sharedViewModel<SyncAnalyzedDiscountsViewModel>(navController)
            else -> it.sharedViewModel<SyncAnalyzedExtensionsViewModel>(navController)
        }

        CommonAlertDialog(
            onDismiss = { navController.popBackStack() },
            dismissText = "Cancelar",
            title = "Confirmação",
            text = "Deseja realizar a exclusão da sincronização do item?",
            onConfirmation = {
                viewModel.removeDataFromSync(args.id) {
                    navController.navigate(SyncDiscountsUpdateNav) {
                        launchSingleTop = true
                    }
                }
            },
        )
    }
}