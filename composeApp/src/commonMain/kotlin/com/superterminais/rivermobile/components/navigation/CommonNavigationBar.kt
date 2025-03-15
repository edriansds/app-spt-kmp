package com.superterminais.rivermobile.components.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.superterminais.rivermobile.navigation.TopLevelRoute

@Composable
fun CommonNavigationBar(items: List<TopLevelRoute>, destination: NavDestination?, selectItem: (Int) -> Unit) {
    Column {
        HorizontalDivider()
        Row(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title
                        )
                    },
                    label = { Text(item.title) },
                    selected = destination?.hierarchy?.any { it.hasRoute(item.route::class) } == true,
                    onClick = {
                        index.also {
                            selectItem(it)
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.secondary,
                        indicatorColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }
    }
}