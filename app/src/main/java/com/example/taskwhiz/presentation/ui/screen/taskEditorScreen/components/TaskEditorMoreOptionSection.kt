package com.example.taskwhiz.presentation.ui.screen.taskEditorScreen.components

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
fun TaskEditorMoreOptionSection(
    dueDate: Long?,
    reminderDate: Long?,
    isHighPriority: Boolean,
    onSetDueClick: () -> Unit,
    onSetReminderClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hasAnything = (dueDate != null || reminderDate != null || isHighPriority)
    if (!hasAnything) return

    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(AppDimens.PaddingMedium),
        verticalAlignment = Alignment.CenterVertically,
        contentPadding = PaddingValues(horizontal = AppDimens.PaddingSmall)
    ) {
        if (dueDate != null) {
            item {
                InputChip(
                    selected = true,
                    onClick = onSetDueClick,
                    label = { Text(dueDate.toDateOnly()) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.DateRange,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    colors = InputChipDefaults.inputChipColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                )
            }
        }

        if (reminderDate != null) {
            item {
                InputChip(
                    selected = true,
                    onClick = onSetReminderClick,
                    label = { Text(reminderDate.toFullDateTime()) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Notifications,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    colors = InputChipDefaults.inputChipColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                )
            }
        }

        if (isHighPriority) {
            item {
                InputChip(
                    selected = true,
                    onClick = onSetReminderClick,
                    label = { Text(stringResource(R.string.high_priority)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = stringResource(R.string.high_priority),
                            tint = MaterialTheme.colorScheme.tertiaryContainer
                        )
                    },
                    colors = InputChipDefaults.inputChipColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        selectedContainerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.1f)
                    )
                )
            }
        }
    }
}

