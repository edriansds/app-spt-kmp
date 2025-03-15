package com.superterminais.rivermobile.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun MainNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    changeTitle: (String) -> Unit,
    showReturnIcon: (Boolean) -> Unit
) {
    NavHost(navController = navController,
        startDestination = DiscountListNav,
        modifier = modifier,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        discountNavGraph(navController, changeTitle, showReturnIcon)
        storageExtensionNavGraph(navController, changeTitle, showReturnIcon)
        syncNavGraph(navController, changeTitle, showReturnIcon)
    }
}