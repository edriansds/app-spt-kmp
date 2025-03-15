package com.superterminais.rivermobile.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme()

private val LightColorScheme = lightColorScheme(
    primary = Color(0xff00a09b),
    primaryContainer = Color(0xFF00C6B5),
    secondary = Color(0xFFE0E0E0),
    tertiary = Color(0xFF00526b),

    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
//    onPrimary = Color.White,
    onSecondary = Color.Gray,
//    onTertiary = Color.White,
//    onBackground = Color(0xFF1C1B1F),
//    onSurface = Color(0xFF1C1B1F),
)

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkColorScheme else LightColorScheme
    ) {
        content()
    }
}