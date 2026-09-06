package com.taskflow.app.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    onPrimary = Purple20,
    primaryContainer = Purple30,
    onPrimaryContainer = Purple90,
    secondary = Teal80,
    onSecondary = Teal10,
    secondaryContainer = Teal20,
    onSecondaryContainer = Teal90,
    tertiary = Amber80,
    onTertiary = Neutral10,
    background = Neutral10,
    onBackground = Neutral99,
    surface = Neutral10,
    onSurface = Neutral99,
    surfaceVariant = Neutral20,
    onSurfaceVariant = Neutral90,
    error = Red80,
    onError = Red40
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    onPrimary = Purple99,
    primaryContainer = Purple90,
    onPrimaryContainer = Purple10,
    secondary = Teal40,
    onSecondary = Teal10,
    secondaryContainer = Teal90,
    onSecondaryContainer = Teal10,
    tertiary = Amber40,
    onTertiary = Neutral10,
    background = Neutral99,
    onBackground = Neutral10,
    surface = Neutral99,
    onSurface = Neutral10,
    surfaceVariant = Neutral90,
    onSurfaceVariant = Neutral20,
    error = Red40,
    onError = Neutral99
)

@Composable
fun TaskFlowTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
