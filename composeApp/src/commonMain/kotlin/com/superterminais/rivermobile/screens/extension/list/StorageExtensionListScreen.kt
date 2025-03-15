package com.superterminais.rivermobile.screens.extension.list

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
import com.superterminais.rivermobile.components.FilterChipRow
import com.superterminais.rivermobile.components.SwipeBox
import com.superterminais.rivermobile.components.screens.CommonInfoScreen
import com.superterminais.rivermobile.components.screens.CommonLoadingScreen
import com.superterminais.rivermobile.components.text.CommonSearchBar
import com.superterminais.rivermobile.data.extension.StorageExtension
import com.superterminais.rivermobile.utils.StringFormatUtils
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun StorageExtensionListScreen(
    viewModel: StorageExtensionListViewModel = koinViewModel<StorageExtensionListViewModel>(),
    modifier: Modifier = Modifier,
    showStorageExtensionDetails: (Int) -> Unit = {},
) {

    LaunchedEffect(Unit) {
        viewModel.fetchStorageExtensions()
    }

    val state by viewModel.uiState.collectAsState()
    val userQuery by viewModel.userQuery.collectAsStateWithLifecycle()
    val selectedSituation by viewModel.selectedStatus.collectAsStateWithLifecycle()

    Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
        Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            CommonSearchBar(query = userQuery,
                onQueryChange = { viewModel.updateUserQuery(it) },
                onQueryCleaned = { viewModel.updateUserQuery("") })
            FilterChipRow(items = viewModel.availableSituationsForFiltering,
                selected = selectedSituation,
                onSelectedChange = { viewModel.toggleSituation(it) })
        }

        when (val uiState = state) {
            is StorageExtensionListViewModel.UiState.Loading -> {
                CommonLoadingScreen()
            }

            is StorageExtensionListViewModel.UiState.Error -> {
                CommonInfoScreen(text = uiState.message)
            }

            is StorageExtensionListViewModel.UiState.Content -> {

                if (uiState.extensions.isEmpty()) {
                    CommonInfoScreen(text = "Nenhuma extensão")
                }

                StorageExtensionList(
                    extensions = uiState.extensions,
                    modifier = modifier.padding(),
                    onSwipe = { id, situation ->
                        viewModel.includeStorageExtensionForSync(
                            id, situation
                        )
                    },
                    selectExtension = showStorageExtensionDetails
                )
            }


        }
    }
}

@Composable
fun StorageExtensionList(
    extensions: List<StorageExtension>,
    modifier: Modifier,
    onSwipe: (Int, String) -> Unit,
    selectExtension: (Int) -> Unit
) {
    LazyColumn {
        items(extensions, key = { it.id }) { extension ->
            StorageExtensionItemList(
                extension = extension,
                modifier = modifier.animateItem(),
                onSwipe = { onSwipe(extension.id, it) },
                enableSwipe = extension.situation == "PENDENTE",
                selectExtension = selectExtension
            )
        }
    }
}

@Composable
fun StorageExtensionItemList(
    extension: StorageExtension,
    modifier: Modifier,
    onSwipe: (String) -> Unit,
    enableSwipe: Boolean = true,
    selectExtension: (Int) -> Unit
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
                text = "${extension.id} - ${extension.clientName}",
                fontSize = MaterialTheme.typography.bodyMedium.fontSize
            )
        }, supportingContent = {
            Column {
                Text(
                    text = "CNPJ: ${StringFormatUtils.cnpjFormat(extension.clientDocument)}",
                    fontSize = MaterialTheme.typography.bodySmall.fontSize
                )
                Text(
                    text = extension.type, fontSize = MaterialTheme.typography.bodySmall.fontSize
                )
                Text(
                    "${StringFormatUtils.currencyFormat(extension.lossValue)} (${extension.days} dias)",
                )
            }
        }, trailingContent = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = extension.createdAt,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize.div(1.1f)
                )
                when (extension.situation) {
                    "APROVADO" -> Icon(
                        Icons.Filled.CheckCircle,
                        contentDescription = "Aprovado",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )

                    "REPROVADO", "CANCELADO" -> Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = extension.situation,
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
            selectExtension(extension.id)
        })
    }
}