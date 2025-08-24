package com.example.taskwhiz.presentation.ui.screen.taskListScreen.components
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.material.icons.Icons

import androidx.compose.material3.Icon

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.taskwhiz.presentation.ui.theme.AppDimens


@Composable
fun StatusBadge(
    text: String,
    icon: ImageVector,
    iconTint: Color = MaterialTheme.colorScheme.primary,
    containerColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Surface(
        color = containerColor,
        shape = RoundedCornerShape(AppDimens.CornerSmall),
        modifier = modifier.height(AppDimens.BadgeHeight),
        tonalElevation = AppDimens.BorderThin
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AppDimens.BadgeSpacing),
            modifier = Modifier.padding(
                horizontal = AppDimens.BadgePaddingHorizontal,
                vertical = AppDimens.BadgePaddingVertical
            )
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(AppDimens.IconSmall),
                tint = iconTint
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                color = contentColor,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StatusBadgePreview() {
    MaterialTheme {
        StatusBadge(
            text = "Dummy Text",
            icon = Icons.Default.Star
        )
    }
}
