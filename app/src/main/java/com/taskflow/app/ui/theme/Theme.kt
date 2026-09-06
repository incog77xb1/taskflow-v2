package com.taskflow.app.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val NeoBrutalistColorScheme = lightColorScheme(
    primary = NeoDark,
    onPrimary = NeoSurface,
    primaryContainer = NeoYellow,
    onPrimaryContainer = NeoDark,
    secondary = NeoPink,
    onSecondary = NeoDark,
    secondaryContainer = NeoBlue,
    onSecondaryContainer = NeoDark,
    tertiary = NeoGreen,
    onTertiary = NeoDark,
    background = NeoBackground,
    onBackground = NeoDark,
    surface = NeoSurface,
    onSurface = NeoDark,
    surfaceVariant = NeoBackground,
    onSurfaceVariant = NeoDark,
    outline = NeoBorder,
    outlineVariant = NeoBorder
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
