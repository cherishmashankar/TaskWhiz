package com.example.taskwhiz.presentation.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskwhiz.domain.model.AppTheme
import com.example.taskwhiz.domain.model.Language

@Composable
fun SettingsMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    currentLanguage: Language,
    currentTheme: AppTheme,
    onLanguageChange: (Language) -> Unit,
    onThemeChange: (AppTheme) -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss
    ) {
        // -------- Language Section --------
        DropdownMenuItem(
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            text = {
                Text(
                    "Language",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            },
            onClick = { /* acts as section header, no-op */ },
            enabled = false
        )

        Language.entries.forEach { lang ->
            DropdownMenuItem(
                leadingIcon = {
                    if (currentLanguage == lang) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Selected",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        Spacer(modifier = Modifier.width(24.dp)) // keep alignment
                    }
                },
                text = { Text(lang.name) },
                onClick = {
                    onLanguageChange(lang)
                    onDismiss()
                }
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 2.dp),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
            thickness = 1.dp
        )

        // -------- Theme Section --------
        DropdownMenuItem(
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.DarkMode,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            text = {
                Text(
                    "Theme",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            },
            onClick = { /* acts as section header, no-op */ },
            enabled = false
        )

        AppTheme.entries.forEach { theme ->
            val icon = when (theme) {
                AppTheme.DARK -> Icons.Default.DarkMode
                AppTheme.LIGHT -> Icons.Default.LightMode
            }
            DropdownMenuItem(
                leadingIcon = {
                    if (currentTheme == theme) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Selected",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        Icon(
                            imageVector = icon,
                            contentDescription = theme.name,
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                },
                text = { Text(theme.name) },
                onClick = {
                    onThemeChange(theme)
                    onDismiss()
                }
            )
        }
    }
}
