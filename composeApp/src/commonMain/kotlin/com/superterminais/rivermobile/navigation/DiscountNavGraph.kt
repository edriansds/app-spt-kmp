package com.superterminais.rivermobile.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.toRoute
import com.superterminais.rivermobile.components.CommonAlertDialog
import com.superterminais.rivermobile.screens.discount.details.DiscountDetailsScreen
import com.superterminais.rivermobile.screens.discount.list.DiscountListScreen
import com.superterminais.rivermobile.screens.discount.list.DiscountListViewModel
import kotlinx.serialization.Serializable


@Serializable
object DiscountListNav

@Serializable
data class DiscountDetailsNav(val discountId: Int)

@Serializable
data class DiscountInfoDialog(
    val title: String = "Aviso",
    val message: String,
    val isError: Boolean = false,
)

fun NavGraphBuilder.discountNavGraph(
    navController: NavHostController,
    changeTitle: (String) -> Unit,
    showReturnIcon: (Boolean) -> Unit
) {
    composable<DiscountListNav> {
        changeTitle("Descontos")
        showReturnIcon(false)

        DiscountListScreen(
            showDiscountDetails = { discountId ->
                navController.navigate(DiscountDetailsNav(discountId)) {
                    launchSingleTop = true
                }
            }, openDialog = { message ->
                navController.navigate(
                    DiscountInfoDialog(message = message)
                )
            })
    }
    composable<DiscountDetailsNav> {
        changeTitle("Detalhes do desconto")
        showReturnIcon(true)

        val args = it.toRoute<DiscountDetailsNav>()
        DiscountDetailsScreen(discountId = args.discountId, openDialog = { title, message ->
            navController.navigate(
                DiscountInfoDialog(
                    title = title,
                    message = message
                )
            )
        })
    }
    dialog<DiscountInfoDialog> {
        val args = it.toRoute<DiscountInfoDialog>()
        CommonAlertDialog(
            onDismiss = {
                navController.navigate(DiscountListNav) {
                    launchSingleTop = true
                }
            },
            title = args.title,
            text = args.message,
            isError = args.isError
        )
    }
}