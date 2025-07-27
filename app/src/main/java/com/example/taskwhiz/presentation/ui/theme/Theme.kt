package com.example.taskwhiz.presentation.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext


// Light Theme Colors
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF4CAF50),             // Green primary
    onPrimary = Color.White,
    background = Color(0xFFF9F9F9),           // Soft off-white
    surface = Color.White,
    onSurface = Color(0xFF333333),            // Dark text
    secondary = Color(0xFFFF9800),            // Accent (orange)
    onSecondary = Color.White,
    error = Color(0xFFD32F2F),
    onError = Color.White
)

// Dark Theme Colors
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF81C784),              // Lighter green
    onPrimary = Color.Black,
    background = Color(0xFF121212),           // Dark background
    surface = Color(0xFF1E1E1E),              // Card-like surface
    onSurface = Color.White,
    secondary = Color(0xFFFFB74D),
    onSecondary = Color.Black,
    error = Color(0xFFEF5350),
    onError = Color.Black
)
@Composable
fun TaskWhizTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicLightColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> LightColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}