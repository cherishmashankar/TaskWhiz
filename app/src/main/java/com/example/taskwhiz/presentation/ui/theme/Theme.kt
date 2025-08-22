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
    primary = Color(0xFF6750A4),      // Deep modern purple (Material baseline)
    onPrimary = Color.White,
    background = Color(0xFFFAF9FC),   // Neutral off-white with a hint of lavender
    onBackground = Color(0xFF1A1A1A), // High contrast dark text
    surface = Color(0xFFFFFFFF),      // Pure white card surfaces
    onSurface = Color(0xFF1E1A22),
    secondary = Color(0xFF9C27B0),    // Rich accent purple (vivid but controlled)
    onSecondary = Color.White,
    error = Color(0xFFB3261E),        // Accessible red (Material baseline)
    onError = Color.White,
    surfaceVariant = Color(0xFFE7E0EC), // Muted lavender-gray for contrast sections
    onSurfaceVariant = Color(0xFF49454F),
    tertiaryContainer = Color(0xFFFFB300), // Warm amber accent
    onTertiaryContainer = Color(0xFF3E2723)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFD0BCFF),      // Bright violet for contrast
    onPrimary = Color(0xFF381E72),    // Dark violet text over light purple
    background = Color(0xFF0D0D0D),   // True dark mode background
    onBackground = Color(0xFFECE6F0), // Soft lavender-white text
    surface = Color(0xFF2C2C2E),    // Elevated card surfaces
    onSurface = Color(0xFFECE6F0),
    secondary = Color(0xFF7C4DFF),    // Bold purple accent
    onSecondary = Color.Black,
    error = Color(0xFFF2B8B5),        // Lighter red for dark mode
    onError = Color(0xFF601410),
    surfaceVariant = Color(0xFF2C2C2C), // Layer separation
    onSurfaceVariant = Color(0xFFCAC4D0),
    tertiaryContainer = Color(0xFFFFD54F), // Warm golden highlight
    onTertiaryContainer = Color(0xFF1A1200)
)

@Composable
fun TaskWhizTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
