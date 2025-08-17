package com.example.taskwhiz.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.FilterChip
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
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
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        contentPadding = PaddingValues(horizontal = 0.dp)
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
                    text = "High Priority",
                    icon = Icons.Default.StarBorder,
                    iconTint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

