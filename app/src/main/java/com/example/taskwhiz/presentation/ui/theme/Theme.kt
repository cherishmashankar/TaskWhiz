package com.example.taskwhiz.presentation.ui.theme


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6750A4),
    onPrimary = Color.White,
    background = Color(0xFFFAF9FC),
    onBackground = Color(0xFF1A1A1A),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1E1A22),
    // Secondary can stay rich but we won’t use it as the selected fill
    secondary = Color(0xFF5B4EA5),
    onSecondary = Color.White,
    surfaceVariant = Color(0xFFEDE7F3),   // slightly lighter than E7E0EC
    onSurfaceVariant = Color(0xFF514A5A),
    error = Color(0xFFB3261E),
    onError = Color.White,
    tertiaryContainer = Color(0xFFFFB300),
    onTertiaryContainer = Color(0xFF3E2723)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFD0BCFF),
    onPrimary = Color(0xFF381E72),
    background = Color(0xFF0D0D0D),
    onBackground = Color(0xFFECE6F0),
    surface = Color(0xFF2C2C2E),
    onSurface = Color(0xFFECE6F0),
    secondary = Color(0xFFD6C9F8),
    onSecondary = Color.Black,
    surfaceVariant = Color(0xFF2F2A36),
    onSurfaceVariant = Color(0xFFCAC4D0),
    error = Color(0xFFF56159),
    onError = Color(0xFF601410),
    tertiaryContainer = Color(0xFFFFD54F),
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
