package com.superterminais.rivermobile.screens.extension.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rivermobile.components.text.TextValueRow
import com.example.rivermobile.ui.theme.Typography
import com.superterminais.rivermobile.components.RoundedButtonsRow
import com.superterminais.rivermobile.components.layout.CommonHorizontalDivider
import com.superterminais.rivermobile.components.screens.CommonInfoScreen
import com.superterminais.rivermobile.components.screens.CommonLoadingScreen
import com.superterminais.rivermobile.components.text.TextValueColumn
import com.superterminais.rivermobile.data.extension.StorageExtension
import com.superterminais.rivermobile.utils.StringFormatUtils
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun StorageExtensionDetailsScreen(
    viewModel: StorageExtensionDetailsViewModel = koinViewModel<StorageExtensionDetailsViewModel>(),
    extensionId: Int,
    openDialog: (String, String) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchStorageExtensionData(extensionId)
    }

    when (val viewState = state) {
        is StorageExtensionDetailsViewModel.UiState.Loading -> {
            CommonLoadingScreen()
        }

        is StorageExtensionDetailsViewModel.UiState.Content -> {
            StorageExtensionDetails(viewState.data, viewModel)

            LaunchedEffect(viewState.updateResult) {
                when (val updateResult = viewState.updateResult) {
                    is StorageExtensionDetailsViewModel.UpdateResult.Success -> openDialog(
                        "Sucesso", updateResult.message
                    )

                    is StorageExtensionDetailsViewModel.UpdateResult.Error -> openDialog(
                        "Erro", updateResult.message
                    )

                    else -> {}
                }
            }
        }

        is StorageExtensionDetailsViewModel.UiState.Error -> {
            CommonInfoScreen(text = viewState.message)
        }
    }
}

@Composable
fun StorageExtensionDetails(
    extension: StorageExtension, viewModel: StorageExtensionDetailsViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextValueRow("Status", extension.situation)

        CommonHorizontalDivider()

        TextValueRow("ID", extension.id.toString())

        TextValueColumn("Cliente", extension.clientName)

        TextValueRow("CNPJ", StringFormatUtils.cnpjFormat(extension.clientDocument))

        TextValueRow("Tipo", extension.type)

        TextValueRow("Período", "${extension.period}º período")

        TextValueRow("Dias Solicitados", "${extension.days}º Dia(s)")

        TextValueColumn("Motivo", extension.reason ?: "")

        TextValueColumn("Contêineres", extension.containers)

        CommonHorizontalDivider()

        StorageExtensionLostValue(extension)

        val actions by viewModel.availableActions.collectAsState()
        if (actions.isNotEmpty()) {
            RoundedButtonsRow(actions) {
                viewModel.includeStorageExtensionForSync(extension.id, it)
            }
        }
    }
}

@Composable
fun StorageExtensionLostValue(extension: StorageExtension) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Valor Perdido", style = Typography.titleLarge, fontSize = 30.sp
        )

        Text(
            text = StringFormatUtils.currencyFormat(extension.lossValue),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
    }
}