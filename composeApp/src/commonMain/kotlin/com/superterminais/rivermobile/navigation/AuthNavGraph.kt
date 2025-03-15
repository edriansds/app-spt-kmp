package com.superterminais.rivermobile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.superterminais.rivermobile.components.CommonAlertDialog
import com.superterminais.rivermobile.screens.auth.LoginScreen
import kotlinx.serialization.Serializable

@Serializable
object Auth

@Serializable
object Login

@Serializable
data class AuthInfoDialog(
    val title: String = "Aviso", val message: String, val isError: Boolean = false
)

fun NavGraphBuilder.authNavGraph(navController: NavController, navigateToMain: () -> Unit) {
    navigation<Auth>(
        startDestination = Login
    ) {
        composable<Login> {
            LoginScreen(onLogin = {
                navigateToMain()
            }, openDialog = {
                navController.navigate(AuthInfoDialog(message = it))
            })
        }
        dialog<AuthInfoDialog> {
            val args = it.toRoute<AuthInfoDialog>()
            CommonAlertDialog(
                onDismiss = { navController.popBackStack() },
                title = args.title,
                text = args.message,
                isError = args.isError
            )
        }
    }
}