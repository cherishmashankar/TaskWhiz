package com.example.taskwhiz.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import com.example.taskwhiz.utils.toDateOnly
import com.example.taskwhiz.utils.toFullDateTime



@Composable
fun ScheduleSection(
    dueDate: Long?,
    reminderDate: Long?,
    onSetDueClick: () -> Unit,
    onClearDue: () -> Unit,
    onSetReminderClick: () -> Unit,
    onClearReminder: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        InputChip(
            selected = dueDate != null,
            onClick = onSetDueClick,
            label = {
                Text(
                    text = dueDate?.toDateOnly() ?: "Set Due Date"
                )

            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = null,
                    tint = if (dueDate != null) MaterialTheme.colorScheme.secondary
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            },
            trailingIcon = if (dueDate != null) {
                {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Clear due date",
                        modifier = Modifier
                            .size(18.dp)
                            .clickable { onClearDue() },
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            } else null,
            colors = InputChipDefaults.inputChipColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
            )
        )


        Spacer(Modifier.width(4.dp))

        InputChip(
            selected = reminderDate != null,
            onClick = onSetReminderClick,
            label = {
                Text(
                    text = reminderDate?.toFullDateTime() ?: "Set Reminder"
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = null,
                    tint = if (reminderDate != null) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            },
            trailingIcon = if (reminderDate != null) {
                {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Clear reminder",
                        modifier = Modifier
                            .size(18.dp)
                            .clickable { onClearReminder() },
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            } else null,
            colors = InputChipDefaults.inputChipColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
            )
        )
    }
}


