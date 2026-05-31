package com.example.taskwhiz.presentation.ui.taskListScreen.components


import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.taskwhiz.presentation.ui.theme.AppDimens
import com.example.taskwhiz.R


@Composable
fun TaskWhizFloatingActionButton(
    onAddTaskClick: () -> Unit,
    onSmartTaskClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.End
    ) {

        val infiniteTransition = rememberInfiniteTransition(label = "fabPulse")

        val scale by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1.08f,
            animationSpec = infiniteRepeatable(
                animation = tween(900, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "scale"
        )

        FloatingActionButton(
            onClick = onSmartTaskClick,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.scale(scale)
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = "Smart AI Task",
                modifier = Modifier.size(AppDimens.IconLarge)
            )
        }

        FloatingActionButton(
            onClick = onAddTaskClick,
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_task_add),
                contentDescription = stringResource(id = R.string.add_task),
                modifier = Modifier.size(AppDimens.IconLarge)
            )
        }
    }
}