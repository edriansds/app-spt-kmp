package com.superterminais.rivermobile.screens.synchronization

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.rivermobile.components.text.TextValueRow
import com.superterminais.rivermobile.components.FilterChipRow
import com.superterminais.rivermobile.components.PrimaryIconButtonColors
import com.superterminais.rivermobile.components.screens.CommonInfoScreen
import com.superterminais.rivermobile.components.screens.CommonLoadingScreen
import com.superterminais.rivermobile.utils.StringFormatUtils
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SynchronizationScreen(
    openManualSyncDialogConfirmation: (String) -> Unit,
    openRemoveSyncDialogConfirmation: (Int, String) -> Unit
) {
    var showFloatingButton by remember { mutableStateOf(false) }

    var selectedFilter by remember { mutableStateOf("Descontos") }
    val activitiesForSync by remember { mutableStateOf(listOf("Descontos", "Extensões")) }

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp), floatingActionButton = {
            if (!showFloatingButton) return@Scaffold
            IconButton(
                onClick = { openManualSyncDialogConfirmation(selectedFilter) },
                colors = PrimaryIconButtonColors,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Sincronizar",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }, floatingActionButtonPosition = FabPosition.EndOverlay
    ) { innerPadding ->
        Column {
            val viewModel: ISyncViewModel = when (selectedFilter) {
                "Descontos" -> koinViewModel<SyncAnalyzedDiscountsViewModel>()
                "Extensões" -> koinViewModel<SyncAnalyzedExtensionsViewModel>()
                else -> koinViewModel<SyncAnalyzedDiscountsViewModel>()
            }

            FilterChipRow(items = activitiesForSync,
                selected = selectedFilter,
                onSelectedChange = { selectedFilter = it })

            when (viewModel) {
                is SyncAnalyzedDiscountsViewModel -> {
                    val state by viewModel.uiState.collectAsState()

                    DiscountsForSync(state = state,
                        modifier = Modifier.padding(innerPadding),
                        getSyncData = { viewModel.getDataPendingForSync() },
                        openRemoveSyncDialogConfirmation = {
                            openRemoveSyncDialogConfirmation(it, selectedFilter)
                        },
                        showFloatingButton = { showFloatingButton = it })
                }

                is SyncAnalyzedExtensionsViewModel -> {
                    val state by viewModel.uiState.collectAsState()
                    ExtensionsForSync(state = state,
                        modifier = Modifier.padding(innerPadding),
                        getSyncData = { viewModel.getDataPendingForSync() },
                        openRemoveSyncDialogConfirmation = {
                            openRemoveSyncDialogConfirmation(it, selectedFilter)
                        },
                        showFloatingButton = { showFloatingButton = it })
                }
            }
        }
    }
}

@Composable
fun DiscountsForSync(
    state: SyncAnalyzedDiscountsViewModel.UiState,
    modifier: Modifier = Modifier,
    getSyncData: () -> Unit,
    openRemoveSyncDialogConfirmation: (Int) -> Unit,
    showFloatingButton: (Boolean) -> Unit
) {

    LaunchedEffect(Unit) {
        getSyncData()
    }

    Column(modifier = modifier) {
        when (state) {
            is SyncAnalyzedDiscountsViewModel.UiState.Loading -> {
                CommonLoadingScreen()
            }

            is SyncAnalyzedDiscountsViewModel.UiState.Error -> {
                CommonInfoScreen(
                    text = state.message
                )
            }

            is SyncAnalyzedDiscountsViewModel.UiState.Content -> {
                SyncronizationInfo(
                    lastSyncDate = state.lastSyncDate
                )

                if (state.discounts.isEmpty()) {
                    CommonInfoScreen(
                        text = "Nenhum desconto para sincronização"
                    )
                } else {
                    showFloatingButton(true)
                }

                val lazyListState = rememberLazyListState()

                LazyColumn(state = lazyListState) {
                    items(state.discounts, key = { it.id }) { discount ->
                        ListItem(modifier = Modifier.animateItem(), leadingContent = {
                            ItemStatusForSyncIcon(discount.statusForSync ?: "")
                        }, headlineContent = {
                            Text(
                                text = "${discount.id} - ${discount.clientName}",
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize
                            )
                        }, supportingContent = {
                            Text(
                                "${StringFormatUtils.currencyFormat(discount.value)} (${
                                    StringFormatUtils.percentageFormat(
                                        discount.discountPercentage
                                    )
                                })",
                            )
                        }, trailingContent = {
                            Box(
                                modifier = Modifier.fillMaxHeight(),
                                contentAlignment = Alignment.Center
                            ) {
                                IconButton(onClick = {
                                    openRemoveSyncDialogConfirmation(discount.id)
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Deletar",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun ExtensionsForSync(
    state: SyncAnalyzedExtensionsViewModel.UiState,
    modifier: Modifier = Modifier,
    getSyncData: () -> Unit,
    openRemoveSyncDialogConfirmation: (Int) -> Unit,
    showFloatingButton: (Boolean) -> Unit
) {

    LaunchedEffect(Unit) {
        getSyncData()
    }

    Column(modifier = modifier) {
        when (state) {
            is SyncAnalyzedExtensionsViewModel.UiState.Loading -> {
                CommonLoadingScreen()
            }

            is SyncAnalyzedExtensionsViewModel.UiState.Error -> {
                CommonInfoScreen(
                    text = state.message
                )
            }

            is SyncAnalyzedExtensionsViewModel.UiState.Content -> {
                SyncronizationInfo(
                    lastSyncDate = state.lastSyncDate
                )

                if (state.storageExtensions.isEmpty()) {
                    CommonInfoScreen(
                        text = "Nenhuma extensão para sincronização"
                    )
                } else {
                    showFloatingButton(true)
                }

                LazyColumn {
                    items(state.storageExtensions, key = { it.id }) { extension ->
                        ListItem(modifier = Modifier.animateItem(), leadingContent = {
                            ItemStatusForSyncIcon(extension.statusForSync)
                        }, headlineContent = {
                            Text(
                                text = "${extension.id} - ${extension.clientName}",
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize
                            )
                        }, supportingContent = {
                            Text(text = "${StringFormatUtils.percentageFormat(extension.lossValue)} (${extension.days} Dia(s))")
                        }, trailingContent = {
                            Box(
                                modifier = Modifier.fillMaxHeight(),
                                contentAlignment = Alignment.Center
                            ) {
                                IconButton(onClick = {
                                    openRemoveSyncDialogConfirmation(extension.id)
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Deletar",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun SyncronizationInfo(
    lastSyncDate: String?
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        if (lastSyncDate != null) {
            TextValueRow("Última sincronízação:", lastSyncDate)
        }

        Text(
            text = "* Sincronização ocorre a cada 15 minutos",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun ItemStatusForSyncIcon(
    status: String,
) {
    when (status) {
        "APROVADO" -> Icon(
            Icons.Filled.CheckCircle,
            contentDescription = status,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        "REPROVADO" -> Icon(
            Icons.Filled.Clear,
            contentDescription = status,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.error
        )
    }
}