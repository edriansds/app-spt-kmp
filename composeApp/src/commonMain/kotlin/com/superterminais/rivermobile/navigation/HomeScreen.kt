package com.superterminais.rivermobile.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.superterminais.rivermobile.components.CommonDropdownMenu
import com.superterminais.rivermobile.components.CommonMenuItem
import com.superterminais.rivermobile.components.navigation.CommonNavigationBar
import com.superterminais.rivermobile.components.text.CommonTitle
import com.superterminais.rivermobile.data.user.UserSession

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController = rememberNavController(),
    navigateToProfile: () -> Unit,
    logout: () -> Unit
) {
    val userName = remember { UserSession.getName() }
    val version = remember { "1.0" }
    val menuItems by remember {
        mutableStateOf(
            listOf(
                CommonMenuItem(
                    title = userName
                ),
                CommonMenuItem(
                    title = "Perfil",
                    action = navigateToProfile,
                    leadingIcon = Icons.Outlined.Person,
                    divider = true
                ),
                CommonMenuItem(
                    title = "Sair",
                    action = logout,
                    leadingIcon = Icons.AutoMirrored.Outlined.ExitToApp
                ),
                CommonMenuItem(title = "v${version}", divider = true)
            )
        )
    }
    val backStackEntry by navController.currentBackStackEntryAsState()

    val navigationItems = remember { topLevelRoutes }
    var currentTitle by remember { mutableStateOf("Descontos") }
    val showNavigationBar by remember {
        derivedStateOf {
            navigationItems.any {
                it.route.toString().contains(backStackEntry?.destination?.route.toString())
            }
        }
    }
    var showIcon by remember { mutableStateOf(false) }

    Scaffold(modifier = Modifier.background(MaterialTheme.colorScheme.background), topBar = {
        CenterAlignedTopAppBar(title = {
            CommonTitle(currentTitle, showIcon = showIcon, onClick = {
                navController.popBackStack()
            })
        },
            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
            actions = {
                if (!showIcon) {
                    CommonDropdownMenu(menuItems)
                }
            })
    }, bottomBar = {
        if (showNavigationBar) {
            BottomBar(navController, backStackEntry?.destination, navigationItems)
        }
    }) { innerPadding ->
        MainNavGraph(modifier = Modifier.padding(innerPadding),
            navController = navController,
            changeTitle = { title -> currentTitle = title },
            showReturnIcon = { showIcon = it })
    }
}

@Composable
fun BottomBar(
    navController: NavHostController,
    currentDestination: NavDestination?,
    items: List<TopLevelRoute>
) {
    CommonNavigationBar(
        items = items,
        destination = currentDestination,
        selectItem = {
            navController.navigate(items[it].route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        },
    )
}