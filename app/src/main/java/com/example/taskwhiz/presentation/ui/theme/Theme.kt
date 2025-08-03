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





private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF2E7D32),       // Rich green - primary actions
    onPrimary = Color.White,
    background = Color(0xFFF7F7F7),    // Light neutral gray (screen background)
    onBackground = Color(0xFF1C1C1C),
    surface = Color(0x94FFFFFF).copy(alpha = 0.9f), // Softer white, slightly translucent look
    onSurface = Color(0xFF1C1C1C),
    secondary = Color(0xFFFB8C00),     // Warm orange accent
    onSecondary = Color.White,
    error = Color(0xFFE53935),
    onError = Color.White
)


private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF81C784),        // Light green - good contrast on dark
    onPrimary = Color.Black,
    background = Color(0xFF121212),     // Deep dark gray (main background)
    onBackground = Color(0xFFE0E0E0),
    surface = Color(0xFF1E1E1E).copy(alpha = 0.95f), // Softer dark card surface
    onSurface = Color(0xFFE0E0E0),
    secondary = Color(0xFFFFB74D),      // Amber accent
    onSecondary = Color.Black,
    error = Color(0xFFEF5350),
    onError = Color.Black
)

@Composable
fun TaskWhizTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}