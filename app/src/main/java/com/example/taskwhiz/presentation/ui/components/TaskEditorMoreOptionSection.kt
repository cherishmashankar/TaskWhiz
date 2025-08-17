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
import androidx.compose.ui.Alignment
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
    // If nothing to show, collapse gracefully
    val hasAnything = (dueDate != null || reminderDate != null || isHighPriority)
    if (!hasAnything) return

    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        if (dueDate != null) {
            item {
                InputChip(
                    selected = true,
                    onClick = onSetDueClick, // tap to edit
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
                    onClick = onSetReminderClick, // tap to edit
                    label = { Text(reminderDate.toFullDateTime()) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Notifications,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    colors = InputChipDefaults.inputChipColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,


                    )
                )
            }
        }

        if (isHighPriority) {
            item {
                InputChip(
                    selected = true,
                    onClick = onSetReminderClick, // tap to edit
                    label = { Text("High Priority") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.StarBorder,
                            contentDescription = "High Priority",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    colors = InputChipDefaults.inputChipColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,

                        )
                )

            }
        }
    }
}
