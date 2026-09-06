package com.taskflow.app.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val NeoBrutalistColorScheme = lightColorScheme(
    primary = NeoDark,
    onPrimary = NeoWhite,
    primaryContainer = NeoYellow,
    onPrimaryContainer = NeoDark,
    secondary = NeoPink,
    onSecondary = NeoDark,
    secondaryContainer = NeoCyan,
    onSecondaryContainer = NeoDark,
    tertiary = NeoGreen,
    onTertiary = NeoDark,
    background = NeoBackground,
    onBackground = NeoDark,
    surface = NeoWhite,
    onSurface = NeoDark,
    surfaceVariant = NeoBackground,
    onSurfaceVariant = NeoDark,
    outline = NeoDark,
    outlineVariant = NeoDark
)

@Composable
fun TaskFlowTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = NeoBrutalistColorScheme,
        typography = Typography,
        content = content
    )
}
