package com.example.taskwhiz.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt

@Composable
fun ColorPaletteSelector(
    selectedColor: String,
    onColorChange: (String) -> Unit
) {
    val colors = listOf(
        "#FF6F61", // Coral
        "#FFB300", // Amber
        "#2E7D32", // Mint Green
        "#118AB2", // Teal
        "#073B4C", // Navy
        "#EF476F", // Pink Red
        "#8E44AD", // Purple
        "#F4A261", // Soft Orange
        "#06D6A0"  // Bright Aqua Green
    )
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(colors) { color ->
            val parsedColor = Color(color.toColorInt())
            val isSelected = color == selectedColor

            Box(
                modifier = Modifier
                    .size(if (isSelected) 44.dp else 40.dp) // Highlight selection
                    .clip(CircleShape)
                    .background(parsedColor)
                    .border(
                        width = if (isSelected) 4.dp else 1.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.onSurface else Color.Gray.copy(
                            alpha = 0.3f
                        ),
                        shape = CircleShape
                    )
                    .clickable { onColorChange(color) }
                    .padding(2.dp)
            ) {
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = Color.White,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(20.dp)
                    )
                }
            }
        }
    }
}

