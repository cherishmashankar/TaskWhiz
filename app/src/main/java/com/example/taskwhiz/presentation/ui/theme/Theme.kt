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
    secondary = Color(0xFF4C5BB1),
    onSecondary = Color.White,
    background = Color(0xFFFAF9FC),
    onBackground = Color(0xFF1A1A1A),
    surface = Color.White,
    onSurface = Color(0xFF1E1A22),
    surfaceVariant = Color(0xFFE7E0EC),
    onSurfaceVariant = Color(0xFF49454F),
    error = Color(0xFFB3261E),
    onError = Color.White,
    tertiaryContainer = Color(0xFFFFB300),
    onTertiaryContainer = Color(0xFF3E2723)
)
private val DarkColorScheme = darkColorScheme(

    primary = Color(0xFFD9CAFC),
    onPrimary = Color(0xFF381E72),
    secondary = Color(0xFFB0C5FF),
    onSecondary = Color(0xFF002D6F),
    background = Color(0xFF0D0D0D),
    onBackground = Color(0xFFECE6F0),
    surface = Color(0xFF1C1B1F),
    onSurface = Color(0xFFECE6F0),
    surfaceVariant = Color(0xFF514C57),
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
