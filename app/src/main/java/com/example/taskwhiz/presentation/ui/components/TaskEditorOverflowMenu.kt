package com.example.taskwhiz.presentation.ui.components

import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*

@Composable
fun TaskEditorOverflowMenu(
    hasDue: Boolean,
    hasReminder: Boolean,
    onRequestSetDueDate: () -> Unit,
    onRequestSetReminder: () -> Unit,
    onClearDueDate: () -> Unit,
    onClearReminder: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier) {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = "More Options"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            // Add or update actions
            DropdownMenuItem(
                leadingIcon = { Icon(
                    Icons.Filled.DateRange,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary) },
                text = { Text(if (hasDue) "Edit Due Date" else "Add Due Date") },
                onClick = {
                    expanded = false
                    onRequestSetDueDate()
                }
            )
            DropdownMenuItem(
                leadingIcon = { Icon(
                    Icons.Filled.Notifications,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary) },
                text = { Text(if (hasReminder) "Edit Reminder" else "Set Reminder") },
                onClick = {
                    expanded = false
                    onRequestSetReminder()
                }
            )

            // Clear actions
            if (hasDue || hasReminder) {
                HorizontalDivider()
            }
            if (hasDue) {
                DropdownMenuItem(
                    leadingIcon = { Icon(Icons.Filled.Close,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error) },
                    text = { Text("Remove Due Date", color = MaterialTheme.colorScheme.error) },
                    onClick = {
                        expanded = false
                        onClearDueDate()
                    }
                )
            }
            if (hasReminder) {
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(Icons.Filled.Close,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error) },
                    text = { Text("Remove Reminder", color = MaterialTheme.colorScheme.error) },
                    onClick = {
                        expanded = false
                        onClearReminder()
                    }
                )
            }
        }
    }
}
