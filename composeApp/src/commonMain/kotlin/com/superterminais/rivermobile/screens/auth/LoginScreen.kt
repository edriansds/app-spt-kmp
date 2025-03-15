package com.superterminais.rivermobile.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.example.rivermobile.auth.AuthState
import com.example.rivermobile.auth.LoginViewModel
import com.superterminais.rivermobile.components.RoundedButton
import com.superterminais.rivermobile.components.layout.CenteredContentColumn
import com.superterminais.rivermobile.components.layout.CommonCard
import com.superterminais.rivermobile.components.text.CommonEmailTextField
import com.superterminais.rivermobile.components.text.CommonPasswordTextField
import com.superterminais.rivermobile.ui.theme.EnabledButtonColor
import com.superterminais.rivermobile.ui.theme.RiverGradient
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(
    onLogin: () -> Unit,
    openDialog: (String) -> Unit = {}
) {
    val viewModel = koinViewModel<LoginViewModel>()
    val loginState by viewModel.authState.collectAsState()

    LaunchedEffect(loginState) {
        when (val state = loginState) {
            is AuthState.Success -> onLogin()
            is AuthState.Error -> {
                openDialog(state.message)
            }

            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = RiverGradient.reversed(),
                    start = Offset(0f, 0f),
                    end = Offset(1000f, 1000f)
                )
            ), contentAlignment = Alignment.Center
    ) {
        CenteredContentColumn {
//            Image(
//                painter = painterResource(R.mipmap.river_port_hub_logo_foreground),
//                contentDescription = stringResource(R.string.app_name)
//            )
            Spacer(modifier = Modifier.padding(8.dp))

            CommonCard(modifier = Modifier.fillMaxWidth(fraction = 0.8f)) {
                CenteredContentColumn {
                    CommonEmailTextField(
                        value = viewModel.email,
                        onValueChange = { viewModel.onEmailChange(it) }
                    )

                    CommonPasswordTextField(
                        value = viewModel.password,
                        onValueChange = { viewModel.onPasswordChange(it) },
                    )

                    Spacer(modifier = Modifier.padding(8.dp))

                    when (loginState) {
                        is AuthState.Loading -> {
                            CircularProgressIndicator(
                                color = EnabledButtonColor,
                                modifier = Modifier.size(60.dp)
                            )
                        }

                        is AuthState.Success -> {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = "Success",
                                modifier = Modifier.size(60.dp),
                                tint = EnabledButtonColor
                            )
                        }

                        else -> {
                            RoundedButton(
                                onClick = { viewModel.login() },
                                modifier = Modifier.defaultMinSize(
                                    minWidth = 200.dp, minHeight = 60.dp
                                ),
                                enabled = viewModel.loginEnabled
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Lock,
                                    contentDescription = "Login",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }

                }
            }
        }
    }
}
