package com.example.taskwhiz.presentation.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer

import androidx.compose.ui.unit.dp
import com.example.taskwhiz.domain.model.Task
import com.example.taskwhiz.presentation.ui.theme.AppDimens
import com.example.taskwhiz.utils.toFullDateTime

@Composable
fun DeleteBackground(
    dismissState: SwipeToDismissBoxState,
    modifier: Modifier = Modifier
) {
    // Animate into the "destructive" look once the swipe heads toward End->Start
    val isDeleting = remember(dismissState.currentValue, dismissState.targetValue) {
        dismissState.targetValue == SwipeToDismissBoxValue.EndToStart ||
                dismissState.currentValue == SwipeToDismissBoxValue.EndToStart
    }

    val bgColor by animateColorAsState(
        targetValue = if (isDeleting) MaterialTheme.colorScheme.errorContainer
        else MaterialTheme.colorScheme.surfaceVariant,
        label = "swipeBg"
    )
    val iconAlpha by animateFloatAsState(
        targetValue = if (isDeleting) 1f else 0.55f,
        label = "iconAlpha"
    )
    val iconScale by animateFloatAsState(
        targetValue = if (isDeleting) 1f else 0.9f,
        label = "iconScale"
    )

    Row(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(AppDimens.CornerLarge))
            .background(bgColor)
            .padding(horizontal = AppDimens.PaddingLarge),
        horizontalArrangement = Arrangement.End, // LTR & RTL "end"
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Delete affordance on the swipe end
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.Delete,
                contentDescription = "Delete",
                tint = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = iconAlpha),
                modifier = Modifier
                    .size(24.dp)
                    .graphicsLayer {
                        scaleX = iconScale
                        scaleY = iconScale
                    }
            )
            Spacer(Modifier.width(8.dp))
            // Optional: short label improves clarity
            Text(
                text = "Delete",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = iconAlpha)
            )
        }
    }
}
