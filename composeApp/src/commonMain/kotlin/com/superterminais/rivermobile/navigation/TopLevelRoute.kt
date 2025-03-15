package com.superterminais.rivermobile.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

data class TopLevelRoute(
    val title: String,
    val route: @Serializable() Any,
    val icon: ImageVector
)

val topLevelRoutes = listOf(
    TopLevelRoute
        ("Descontos", DiscountListNav, Icons.Filled.ShoppingCart),
    TopLevelRoute
        ("Extensões", StorageExtensionListNav, Icons.Filled.LocationOn),
    TopLevelRoute
        ("Sincronizar", SyncDiscountsUpdateNav, Icons.Filled.Share)
)
