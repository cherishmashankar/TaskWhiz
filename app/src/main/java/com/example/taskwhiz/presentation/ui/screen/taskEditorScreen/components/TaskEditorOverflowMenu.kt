package com.example.taskwhiz.presentation.ui.screen.taskEditorScreen.components

import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import com.example.taskwhiz.R

@Composable
fun TaskEditorOverflowMenu(
    hasDue: Boolean,
    hasReminder: Boolean,
    onRequestSetDueDate: () -> Unit,
    onRequestSetReminder: () -> Unit,
    onClearDueDate: () -> Unit,
    onClearReminder: () -> Unit,
    hasHighPriority: Boolean,
    onSetHighPriority: () -> Unit,
    onClearPriority: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier) {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = stringResource(R.string.more_options)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            // Add / edit
            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        Icons.Filled.DateRange,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                text = { Text(if (hasDue)
                    stringResource(R.string.edit_due_date)
                else
                    stringResource(R.string.add_due_date)) },
                onClick = {
                    expanded = false
                    onRequestSetDueDate()
                }
            )
            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        Icons.Filled.Notifications,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                text = { Text(if (hasReminder) stringResource(R.string.edit_reminder) else stringResource(R.string.set_reminder)) },
                onClick = {
                    expanded = false
                    onRequestSetReminder()
                }
            )

            // Priority section
            HorizontalDivider()
            if (hasHighPriority) {
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                    },
                    text = { Text(stringResource(R.string.remove_priority), color = MaterialTheme.colorScheme.error) },
                    onClick = {
                        expanded = false
                        onClearPriority()
                    }
                )
            } else {
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            imageVector =  Icons.Default.StarBorder,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },

                    text = { Text(stringResource(R.string.set_high_priority)) },
                    onClick = {
                        expanded = false
                        onSetHighPriority()
                    }
                )
            }

            // Clear actions if any
            if (hasDue || hasReminder) {
                HorizontalDivider()
            }
            if (hasDue) {
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                    },
                    text = { Text(stringResource(R.string.remove_due_date), color = MaterialTheme.colorScheme.error) },
                    onClick = {
                        expanded = false
                        onClearDueDate()
                    }
                )
            }
            if (hasReminder) {
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                    },
                    text = { Text(stringResource(R.string.remove_reminder), color = MaterialTheme.colorScheme.error) },
                    onClick = {
                        expanded = false
                        onClearReminder()
                    }
                )
            }
        }
    }
}

