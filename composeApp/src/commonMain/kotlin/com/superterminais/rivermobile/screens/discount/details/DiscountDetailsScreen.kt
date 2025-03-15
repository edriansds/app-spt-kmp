package com.superterminais.rivermobile.screens.discount.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rivermobile.components.text.TextValueRow
import com.example.rivermobile.ui.theme.Typography
import com.superterminais.rivermobile.components.RoundedButtonsRow
import com.superterminais.rivermobile.components.layout.CommonHorizontalDivider
import com.superterminais.rivermobile.components.screens.CommonInfoScreen
import com.superterminais.rivermobile.components.text.TextValueColumn
import com.superterminais.rivermobile.data.discount.DiscountData
import com.superterminais.rivermobile.utils.StringFormatUtils
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DiscountDetailsScreen(
    viewModel: DiscountDetailsViewModel = koinViewModel<DiscountDetailsViewModel>(),
    discountId: Int,
    openDialog: (String, String) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchDiscountData(discountId)
    }

    when (val viewState = state) {
        is DiscountDetailsViewState.SuccessContent -> {
            DiscountDetails(viewState.discountData, viewModel)

            LaunchedEffect(viewState.updateResult) {
                when (val updateResult = viewState.updateResult) {
                    is UpdateResult.Success -> openDialog(
                        "Sucesso", updateResult.message
                    )

                    is UpdateResult.Error -> openDialog("Erro", updateResult.message)
                    else -> {}
                }
            }
        }

        is DiscountDetailsViewState.Error -> {
            CommonInfoScreen(
                text = viewState.message,
            )
        }

        else -> {}
    }
}

@Composable
fun DiscountDetails(
    discount: DiscountData, viewModel: DiscountDetailsViewModel
) {
    val actions = viewModel.availableActions.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextValueRow("Status", discount.situation, modifier = Modifier.padding(8.dp))

        CommonHorizontalDivider()

        DiscountDetailsVisualizationForm(discount)

        HorizontalDivider(
            color = Color.LightGray, thickness = 1.dp, modifier = Modifier.padding(8.dp)
        )

        DiscountValues(discount)

        if (actions.value.isNotEmpty()) {
            RoundedButtonsRow(actions.value) {
                viewModel.updateDiscountSituation(discount.id, it)
            }
        }
    }
}

@Composable
fun DiscountDetailsVisualizationForm(discount: DiscountData) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextValueRow("ID", discount.id.toString())

        TextValueColumn("Cliente", discount.clientName)

        TextValueRow("CNPJ", StringFormatUtils.cnpjFormat(discount.clientDocument))

        TextValueRow("Valor ", StringFormatUtils.currencyFormat(discount.value))

        TextValueRow("Tipo de Desconto", discount.discountTypeDescription ?: "")

        TextValueRow("Desconto", StringFormatUtils.percentageFormat(discount.discountPercentage))

        TextValueColumn("Motivo", discount.reason ?: "")
    }
}

@Composable
fun DiscountValues(discount: DiscountData) {
    val discountValue = (discount.value * discount.discountPercentage) / 100
    val valueAfterDiscount = discount.value - discountValue

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Desconto", style = Typography.titleLarge, fontSize = 30.sp
        )

        Text(
            text = StringFormatUtils.currencyFormat(discountValue),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Valor com desconto: ${StringFormatUtils.currencyFormat(valueAfterDiscount)}",
            fontSize = 14.sp
        )
    }
}