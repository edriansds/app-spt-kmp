package com.superterminais.rivermobile.components.layout

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CommonHorizontalDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(color = Color.LightGray, thickness = 1.dp, modifier = modifier.padding(8.dp))
}