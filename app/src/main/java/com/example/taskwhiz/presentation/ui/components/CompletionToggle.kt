package com.example.taskwhiz.presentation.ui.components


import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt


@Composable
fun CompletionToggle(
    isCompleted: Boolean,
    colorHex: String,
    onToggle: () -> Unit
) {
    val transition = updateTransition(targetState = isCompleted, label = "CompletionTransition")
    val taskColor = Color(colorHex.toColorInt())
    val color by transition.animateColor(label = "ColorTransition") { state ->
        if (state) taskColor else taskColor
    }
    val scale by transition.animateFloat(label = "ScaleTransition") { state ->
        if (state) 1.1f else 1f
    }

    Box(
        modifier = Modifier
            .size(28.dp)
            .scale(scale)
            .clip(CircleShape)
            .border(
                width = if (isCompleted) 0.dp else 4.dp,
                color = if (isCompleted) Color.Transparent else color,
                shape = CircleShape
            )
            .background(if (isCompleted) color else Color.Transparent, CircleShape)
            .clickable { onToggle() },
        contentAlignment = Alignment.Center
    ) {
        if (isCompleted) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Completed",
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}



