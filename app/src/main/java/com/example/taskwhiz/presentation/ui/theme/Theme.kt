package com.example.taskwhiz.presentation.ui.theme


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val LightColorScheme = lightColorScheme(
    // Primary: A slightly deeper emerald green so it stands out sharply on light backgrounds
    primary = Color(0xFF059669),
    onPrimary = Color(0xFFFFFFFF),      // Crisp white text over the green button/check

    // Secondary: A soft, professional sage/slate for chips and tags
    secondary = Color(0xFF10B981),
    onSecondary = Color(0xFFFFFFFF),

    // Background: A clean, bright paper-white (very slight warmth to prevent eye strain)
    background = Color(0xFFFAFAFA),
    onBackground = Color(0xFF111827),   // Deep dark charcoal for highly legible text

    // Surface: Pure white for task cards to make them cleanly pop off the background
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1F2937),

    // Surface Variant: Light grey for unchecked box outlines and dividers
    surfaceVariant = Color(0xFFE5E7EB),
    onSurfaceVariant = Color(0xFF6B7280),

    // Error: A clean, professional coral red
    error = Color(0xFFDC2626),
    onError = Color(0xFFFFFFFF)
)

private val DarkColorScheme = darkColorScheme(
    // Primary: Electric Emerald. Looks incredible against pure black for checkboxes
    primary = Color(0xFF10B981),
    onPrimary = Color(0xFF000000),

    // Secondary: Deep mint-slate for tags/metadata
    secondary = Color(0xFF34D399),
    onSecondary = Color(0xFF064E3B),

    // Background: Pure OLED Black
    background = Color(0xFF000000),
    onBackground = Color(0xFFF3F4F6),   // Crisp off-white text

    // Surface: A dark obsidian grey for task cards so they float sharply
    surface = Color(0xFF111827),
    onSurface = Color(0xFFE5E7EB),

    // Surface Variant: Empty checkbox lines and app borders
    surfaceVariant = Color(0xFF1F2937),
    onSurfaceVariant = Color(0xFF9CA3AF),

    error = Color(0xFFF87171),
    onError = Color(0xFF000000)
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
