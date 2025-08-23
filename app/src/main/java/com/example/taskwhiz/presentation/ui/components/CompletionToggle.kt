package com.example.taskwhiz.presentation.ui.components


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.example.taskwhiz.R
import com.example.taskwhiz.presentation.ui.theme.AppDimens

@Composable
fun CompletionToggle(
    isCompleted: Boolean,
    colorHex: String,
    onToggle: () -> Unit
) {
    val taskColor = Color(colorHex.toColorInt())

    val bgColor by animateColorAsState(
        if (isCompleted) taskColor else Color.Transparent,
        label = "bg"
    )
    val borderColor by animateColorAsState(
        if (isCompleted) taskColor else taskColor.copy(alpha = 0.5f),
        label = "border"
    )
    val borderWidth by animateDpAsState(
        if (isCompleted) 0.dp else AppDimens.BorderNormal,
        label = "bw"
    )
    val shape = RoundedCornerShape(AppDimens.CornerSmall)

    Box(
        modifier = Modifier
            .size(AppDimens.ToggleSize)
            .clip(shape)
            .background(bgColor, shape)
            .border(borderWidth, borderColor, shape)
            .toggleable(
                value = isCompleted,
                onValueChange = { onToggle() },
                role = Role.Checkbox,
            ),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = isCompleted,
            enter = scaleIn() + fadeIn(),
            exit = fadeOut()
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = stringResource(R.string.task_completed),
                tint = Color.White,
                modifier = Modifier.size(AppDimens.IconSmall)
            )
        }
    }
}




