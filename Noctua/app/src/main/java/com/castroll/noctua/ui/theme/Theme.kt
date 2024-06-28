package com.castroll.noctua.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = NavyBlue,
    primaryContainer = DarkBlue,
    secondary = LightGray,
    secondaryContainer = DarkBlue,
    surface = White,
    background = White,
    error = Color.Red,
    onPrimary = White,
    onSecondary = Black,
    onSurface = Black,
    onBackground = Black
)

@Composable
fun MyApplicationTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
