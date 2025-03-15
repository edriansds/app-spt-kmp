package com.superterminais.rivermobile.screens.discount.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.superterminais.rivermobile.components.FilterChipRow
import com.superterminais.rivermobile.components.SwipeBox
import com.superterminais.rivermobile.components.screens.CommonInfoScreen
import com.superterminais.rivermobile.components.screens.CommonLoadingScreen
import com.superterminais.rivermobile.components.text.CommonSearchBar
import com.superterminais.rivermobile.data.discount.DiscountData
import com.superterminais.rivermobile.utils.StringFormatUtils
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DiscountListScreen(
    viewModel: DiscountListViewModel = koinViewModel<DiscountListViewModel>(),
    modifier: Modifier = Modifier,
    showDiscountDetails: (Int) -> Unit = {},
    openDialog: (String) -> Unit = {}
) {

    LaunchedEffect(Unit) {
        viewModel.fetchDiscounts()
    }

    val state by viewModel.uiState.collectAsState()
    val userQuery by viewModel.userQuery.collectAsStateWithLifecycle()
    val selectedSituation by viewModel.selectedSituation.collectAsStateWithLifecycle()

    Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
        Column {
            CommonSearchBar(query = userQuery,
                onQueryChange = { viewModel.updateUserQuery(it) },
                onQueryCleaned = { viewModel.updateUserQuery("") })
            FilterChipRow(items = viewModel.availableSituationsForFiltering,
                selected = selectedSituation,
                onSelectedChange = { viewModel.toggleSituation(it) })
        }

        when (val uiState = state) {
            is DiscountListViewModel.UiState.Loading -> {
                CommonLoadingScreen()
            }

            is DiscountListViewModel.UiState.Error -> {
                openDialog(uiState.message)
            }

            is DiscountListViewModel.UiState.Content -> {

                if (uiState.discounts.isEmpty()) {
                    CommonInfoScreen(text = "Nenhum desconto")
                }

                DiscountList(
                    discounts = uiState.discounts,
                    modifier = modifier.padding(),
                    onSwipe = { id, situation -> viewModel.includeDiscountForSync(id, situation) },
                    selectDiscount = showDiscountDetails
                )
            }
        }
    }
}

@Composable
fun DiscountList(
    discounts: List<DiscountData>,
    modifier: Modifier,
    onSwipe: (Int, String) -> Unit,
    selectDiscount: (Int) -> Unit
) {
    LazyColumn {
        items(discounts, key = { it.id }) { discount ->
            DiscountListItem(
                discount = discount,
                modifier = modifier.animateItem(),
                onSwipe = { onSwipe(discount.id, it) },
                enableSwipe = discount.situation == "PENDENTE",
                selectDiscount = selectDiscount
            )
        }
    }
}

@Composable
fun DiscountListItem(
    discount: DiscountData,
    modifier: Modifier,
    onSwipe: (String) -> Unit,
    enableSwipe: Boolean = true,
    selectDiscount: (Int) -> Unit
) {
    SwipeBox(
        modifier = modifier,
        onSwipeLeft = { onSwipe("APROVADO") },
        onSwipeRight = { onSwipe("REPROVADO") },
        enableSwipeToLeft = enableSwipe,
        enableSwipeToRight = enableSwipe
    ) {
        ListItem(headlineContent = {
            Text(
                text = "${discount.id} - ${discount.clientName}",
                fontSize = MaterialTheme.typography.bodyMedium.fontSize
            )
        }, supportingContent = {
            Column {
                Text(
                    text = "CNPJ: ${StringFormatUtils.cnpjFormat(discount.clientDocument)}",
                    fontSize = MaterialTheme.typography.bodySmall.fontSize
                )
                if (!discount.discountTypeDescription.isNullOrEmpty()) {
                    Text(
                        text = discount.discountTypeDescription,
                        fontSize = MaterialTheme.typography.bodySmall.fontSize
                    )
                }
                Text(
                    "${StringFormatUtils.currencyFormat(discount.value)} (${
                        StringFormatUtils.percentageFormat(
                            discount.discountPercentage
                        )
                    })",
                )
            }
        }, trailingContent = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = discount.creadetAt,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize.div(1.1f)
                )
                when (discount.situation) {
                    "APROVADO" -> Icon(
                        Icons.Filled.CheckCircle,
                        contentDescription = "Aprovado",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )

                    "REPROVADO", "CANCELADO" -> Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = discount.situation,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.error
                    )

                    "PENDENTE" -> {
                        Icon(
                            imageVector = Icons.Filled.Warning,
                            contentDescription = "Pendente",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            }
        }, modifier = Modifier.clickable {
            selectDiscount(discount.id)
        })
    }
}
