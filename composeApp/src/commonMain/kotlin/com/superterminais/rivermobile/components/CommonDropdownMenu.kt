package com.superterminais.rivermobile.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.superterminais.rivermobile.components.layout.CommonHorizontalDivider

data class CommonMenuItem(
    val title: String,
    val leadingIcon: ImageVector? = null,
    val action: () -> Unit = {},
    val divider: Boolean = false
)

@Composable
fun CommonDropdownMenu(items: List<CommonMenuItem>) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .padding(16.dp)
    ) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(Icons.Default.MoreVert, contentDescription = "More options")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { option ->
                if (option.divider) {
                    CommonHorizontalDivider()
                }

                DropdownMenuItem(
                    text = { Text(option.title) },
                    leadingIcon = {
                        option.leadingIcon?.let {
                            Icon(it, contentDescription = option.title)
                        }
                    },
                    onClick = {
                        option.action()
                        expanded = false
                    }
                )
            }
        }
    }
}