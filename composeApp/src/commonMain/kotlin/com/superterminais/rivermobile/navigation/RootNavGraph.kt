package com.superterminais.rivermobile.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import com.superterminais.rivermobile.MainViewModel
import com.superterminais.rivermobile.components.CommonAlertDialog
import com.superterminais.rivermobile.screens.profile.ProfileScreen
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable
object Home

@Serializable
object Profile

@Serializable
object LogoutDialog

@Composable
fun RootNavGraph(
    navController: NavHostController,
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    isUserAuthenticated: Boolean
) {
    NavHost(
        navController = navController,
        startDestination = if (isUserAuthenticated) Home else Auth,
        modifier = modifier,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        authNavGraph(navController) {
            navController.navigate(Home) {
                viewModel.onAuthentication()
                popUpTo(navController.graph.findStartDestination().id) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
        composable<Home> {
            HomeScreen(logout = {
                navController.navigate(LogoutDialog) {
                    launchSingleTop = true
                }

            }, navigateToProfile = {
                navController.navigate(Profile) {
                    launchSingleTop = true
                }
            })
        }
        composable<Profile> {
            ProfileScreen {
                navController.popBackStack()
            }
        }
        dialog<LogoutDialog> {
            CommonAlertDialog(title = "Sair", text = "Deseja realmente sair?", onConfirmation = {
                viewModel.onLogout {
                    navController.navigate(Auth) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            }, onDismiss = {
                navController.popBackStack()
            })
        }
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
    val navGraphRoute = destination.parent?.route ?: return koinViewModel<T>()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry)
}