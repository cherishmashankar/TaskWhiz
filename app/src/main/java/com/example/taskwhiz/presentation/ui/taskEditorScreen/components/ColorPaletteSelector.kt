package com.example.taskwhiz.presentation.ui.taskEditorScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.core.graphics.toColorInt
import com.example.taskwhiz.R
import com.example.taskwhiz.presentation.ui.theme.AppDimens

@Composable
fun ColorPaletteSelector(
    selectedColor: String,
    onColorChange: (String) -> Unit
) {
    val colors = if (isSystemInDarkTheme()) {
        listOf(
            "#E64545", // Deep Coral (Darkened for white contrast)
            "#D9A520", // Goldenrod (Darker yellow/gold to make white pop)
            "#00A37A", // Emerald Green (Stronger contrast than Aqua)
            "#1E90FF", // Dodger Blue (High visibility for white icon)
            "#8A2BE2", // Blue Violet (Bright enough to see, dark enough for white)
            "#D35400", // Pumpkin Orange (Rich contrast)
            "#008B8B", // Dark Cyan (Solid background for white check)
        )

    } else {
        listOf(
            "#FF5252", // Vibrant Red
            "#FBC02D", // Flat Gold (Needs to be dark enough for white icon)
            "#43A047", // Medium Green
            "#1E88E5", // Bright Blue
            "#8E24AA", // Deep Purple (Lighter than your Primary)
            "#FB8C00", // Dark Orange
            "#00897B"  // Teal
        )
    }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(AppDimens.PaddingMedium),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        items(colors) { color ->
            val parsedColor = Color(color.toColorInt())
            val isSelected = color == selectedColor

            Box(
                modifier = Modifier
                    .size(if (isSelected) AppDimens.ColorCircleSelected else AppDimens.ColorCircle)
                    .clip(CircleShape)
                    .background(parsedColor)
                    .border(
                        width = if (isSelected) AppDimens.BorderThick else AppDimens.BorderThin,
                        color = if (isSelected) MaterialTheme.colorScheme.background
                        else Color.Gray.copy(alpha = 0.3f),
                        shape = CircleShape
                    )
                    .clickable { onColorChange(color) }
                    .padding(AppDimens.PaddingXSmall)
            ) {
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = stringResource(R.string.color_selected),
                        tint = Color.White,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(AppDimens.IconMedium)
                    )
                }
            }
        }
    }
}


