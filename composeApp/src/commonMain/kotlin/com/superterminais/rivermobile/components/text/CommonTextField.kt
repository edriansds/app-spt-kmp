package com.superterminais.rivermobile.components.text

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun CommonEmailTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = "E-mail", style = MaterialTheme.typography.bodyMedium
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Next, keyboardType = KeyboardType.Email
        ),
        modifier = Modifier.fillMaxWidth(fraction = 1f)
    )
}

@Composable
fun CommonPasswordTextField(
    value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier
) {
    var togglePasswordVisibility by remember { mutableStateOf(value = false) }
    val passwordVisualTransformation by remember {
        derivedStateOf {
            if (!togglePasswordVisibility) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            }
        }
    }

    val passwordIcon = if (togglePasswordVisibility) {
        Icons.AutoMirrored.Outlined.ExitToApp
    } else {
        Icons.AutoMirrored.Outlined.ExitToApp
    }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = "Senha",
                style = MaterialTheme.typography.bodyMedium
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done, keyboardType = KeyboardType.Password
        ),
        trailingIcon = {
            IconButton(onClick = {
                togglePasswordVisibility = !togglePasswordVisibility
            }) {
                Icon(
                    imageVector = passwordIcon,
                    contentDescription = "Toggle password visibility",
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        visualTransformation = passwordVisualTransformation,
        modifier = Modifier
            .fillMaxWidth(fraction = 1f)
            .then(modifier)
    )
}