package com.parkingmanagerapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF6750A4),
    onPrimary = Color.White,
    secondary = Color(0xFF7C4DFF),
    onSecondary = Color.White,
    background = Color(0xFF121212),
    onBackground = Color.White,
    surface = Color(0xFF1E1E1E),
    onSurface = Color.White,
    primaryContainer = Color(0xFF512DA8), // For occupied slots
    secondaryContainer = Color(0xFFD1C4E9), // For unoccupied slots
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6750A4),
    onPrimary = Color.White,
    secondary = Color(0xFF7C4DFF),
    onSecondary = Color.White,
    background = Color(0xFFF3E5F5), // Lightened for a softer look
    onBackground = Color(0xFF1C1B1F),
    surface = Color(0xFFEDE7F6), // Lightened for consistency with background
    onSurface = Color(0xFF1C1B1F),
    primaryContainer = Color(0xFFD1C4E9), // For occupied slots
    secondaryContainer = Color(0xFFEDE7F6), // For unoccupied slots
)

@Composable
fun ParkingManagerAppTheme(
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
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = AppShapes,
        content = content
    )
}