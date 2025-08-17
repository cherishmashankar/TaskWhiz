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
    primary = Color(0xFF9B6DFF),      // Vibrant purple
    onPrimary = Color.White,
    background = Color(0xFFF4F1FA),   // Soft lavender background
    onBackground = Color(0xFF1E1A22), // Dark text
    surface = Color(0xFFFCFCFC),      // Card surface
    onSurface = Color(0xFF1E1A22),
    secondary = Color(0xFFB794F6),    // Lighter purple accent
    onSecondary = Color.White,
    error = Color(0xFFE57373),
    onError = Color.White,
    surfaceVariant = Color(0xE6F4F1FA),
    tertiaryContainer =Color(0xFFFFB300),
    onTertiaryContainer = Color(0xFF3E2723)


)


private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFB794F6),       // Lighter purple for dark backgrounds
    onPrimary = Color.Black,
    background = Color(0xFF1C1A23),    // Deep purple background
    onBackground = Color(0xFFEDE7F6),  // Soft light text
    surface = Color(0xFF2B2733),       // Slightly elevated surface
    onSurface = Color(0xFFEDE7F6),
    secondary = Color(0xFF9B6DFF),     // Vibrant accent
    onSecondary = Color.Black,
    error = Color(0xFFEF9A9A),
    onError = Color.Black,
    surfaceVariant = Color(0xFF2A2633),
    tertiaryContainer = Color(0xFFFFB300),
    onTertiaryContainer = Color.Black
)
@Composable
fun TaskWhizTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) LightColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}