package com.superterminais.rivermobile.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.superterminais.rivermobile.components.layout.CommonHorizontalDivider
import com.superterminais.rivermobile.components.screens.CommonInfoScreen
import com.superterminais.rivermobile.components.screens.CommonLoadingScreen
import com.superterminais.rivermobile.components.text.CommonTitle
import com.superterminais.rivermobile.components.text.TextValueColumn
import com.superterminais.rivermobile.data.profile.ProfileData
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = koinViewModel<ProfileViewModel>(), onNavigateBack: () -> Unit
) {
    LaunchedEffect(Unit) { viewModel.fetchProfile() }

    Scaffold(modifier = Modifier.background(MaterialTheme.colorScheme.background), topBar = {
        CenterAlignedTopAppBar(title = {
            CommonTitle("Perfil", showIcon = true, onClick = {
                onNavigateBack()
            })
        }, windowInsets = WindowInsets(0.dp))
    }) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()
        when (val state = uiState) {
            is ProfileViewModel.UiState.Loading -> {
                CommonLoadingScreen()
            }

            is ProfileViewModel.UiState.Error -> {
                CommonInfoScreen(
                    text = state.message
                )
            }

            is ProfileViewModel.UiState.Content -> {
                ProfileDetails(profile = state.profile, modifier = Modifier.padding(innerPadding))
            }
        }
    }
}

@Composable
fun ProfileDetails(profile: ProfileData, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .then(modifier),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

//        if (profile.profilePicture != null) {
//            val image = remember { imageBitmapFromBase64(profile.profilePicture) }
//
//            Image(
//                bitmap = image,
//                contentScale = ContentScale.Crop,
//                contentDescription = "Profile Picture",
//                modifier = Modifier.clip(
//                    CircleShape,
//                ).size(200.dp)
//            )
//        }

        CommonHorizontalDivider()

        TextValueColumn(
            label = "Nome", value = profile.name
        )

        TextValueColumn(
            label = "Email", value = profile.email
        )

        TextValueColumn(
            label = "CPF", value = profile.cpf
        )

        CommonHorizontalDivider()

        TextValueColumn(
            label = "Perfil(s)",
            value = profile.roles.joinToString(separator = ", ") { it.roleName }
        )
    }
}