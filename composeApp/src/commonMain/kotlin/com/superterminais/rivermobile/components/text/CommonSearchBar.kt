package com.superterminais.rivermobile.components.text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp

@Composable
fun CommonSearchBar(
    query: String = "", onQueryChange: (String) -> Unit = {}, onQueryCleaned: () -> Unit = {}
) {
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val onSecondaryColor = MaterialTheme.colorScheme.onSecondary

    val rowModifier = remember {
        Modifier
            .clip(RoundedCornerShape(32.dp))
            .fillMaxWidth()
            .padding(8.dp)
            .defaultMinSize(minHeight = 48.dp)
            .background(
                color = secondaryColor, shape = RoundedCornerShape(32.dp)
            )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = rowModifier
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            tint = onSecondaryColor,
            modifier = Modifier.padding(start = 8.dp)
        )
        BasicTextField(
            value = query,
            onValueChange = { onQueryChange(it) },
            singleLine = true,
            modifier = Modifier.weight(1f),
            cursorBrush = SolidColor(onSecondaryColor),
            decorationBox = { innerTextField ->
                if (query.isEmpty()) {
                    Text(
                        text = "Pesquisar",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                    innerTextField()
            }
        )

        if (query.isNotEmpty()) {
            IconButton(onClick = { onQueryCleaned() }) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear",
                    tint = onSecondaryColor,
                )
            }
        }
    }
}