package com.superterminais.rivermobile.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight


@Composable
fun CommonAlertDialog(
    onDismiss: () -> Unit,
    dismissText: String = "Fechar",
    onConfirmation: (() -> Unit)? = null,
    confirmationText: String = "Confirmar",
    title: String = "Alerta",
    isError: Boolean = false,
    text: String,
    icon: ImageVector = Icons.Default.Person,
) {
    AlertDialog(icon = {
        Icon(icon, contentDescription = "Example Icon")
    }, title = {
        val color =
            if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
        Text(text = title, color = color, fontWeight = FontWeight.Bold)
    }, text = {
        Text(text = text)
    }, onDismissRequest = {
        onDismiss()
    }, confirmButton = {
        if (onConfirmation == null) return@AlertDialog
        TextButton(onClick = {
            onConfirmation()
        }) {
            Text(confirmationText)
        }
    }, dismissButton = {
        TextButton(onClick = {
            onDismiss()
        }) {
            Text(dismissText)
        }
    }, containerColor = MaterialTheme.colorScheme.surface
    )
}