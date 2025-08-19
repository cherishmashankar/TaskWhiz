package com.example.taskwhiz.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import com.example.taskwhiz.R
import com.example.taskwhiz.presentation.ui.theme.AppDimens
import com.example.taskwhiz.utils.toDateOnly
import com.example.taskwhiz.utils.toFullDateTime


@Composable
fun TaskStatusBadges(
    dueDate: Long?,
    reminderDate: Long?,
    isHighPriority: Boolean,
    modifier: Modifier = Modifier
) {
    val hasAnything = (dueDate != null || reminderDate != null || isHighPriority)
    if (!hasAnything) return

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(AppDimens.BadgeSpacingRow),
        verticalAlignment = Alignment.CenterVertically,
        contentPadding = PaddingValues(horizontal = 0.dp),
        modifier = modifier
    ) {
        if (dueDate != null) {
            item {
                StatusBadge(
                    text = dueDate.toDateOnly(),
                    icon = Icons.Filled.DateRange,
                    iconTint = MaterialTheme.colorScheme.primary
                )
            }
        }

        if (reminderDate != null) {
            item {
                StatusBadge(
                    text = reminderDate.toFullDateTime(),
                    icon = Icons.Filled.Notifications,
                    iconTint = MaterialTheme.colorScheme.primary
                )
            }
        }

        if (isHighPriority) {
            item {
                StatusBadge(
                    text = stringResource(R.string.high_priority),
                    icon = Icons.Default.Star,
                    iconTint = MaterialTheme.colorScheme.tertiaryContainer,
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.1f)
                )
            }
        }
    }
}



