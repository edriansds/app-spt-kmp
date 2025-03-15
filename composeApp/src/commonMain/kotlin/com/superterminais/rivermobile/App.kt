package com.superterminais.rivermobile

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.superterminais.rivermobile.data.user.UserSession
import com.superterminais.rivermobile.navigation.RootNavGraph
import com.superterminais.rivermobile.ui.theme.AppTheme
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun App() {
    AppTheme {
        Surface {
            val navController: NavHostController = rememberNavController()

            val viewModel: MainViewModel = koinViewModel<MainViewModel>()
            viewModel.verifyAuthentication()

            val isUserAuthenticated = UserSession.getToken() != null

            RootNavGraph(
                navController = navController,
                viewModel = viewModel,
                isUserAuthenticated = isUserAuthenticated
            )
        }
    }
}
