package com.example.taskwhiz.presentation.ui.taskEditorScreen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import com.example.taskwhiz.R
import java.util.Calendar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    selectedDateMillis: Long,
    onTimeSelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val cal = remember(selectedDateMillis) {
        Calendar.getInstance().apply { timeInMillis = selectedDateMillis }
    }

    val state = rememberTimePickerState(
        initialHour = cal.get(Calendar.HOUR_OF_DAY),
        initialMinute = cal.get(Calendar.MINUTE),
        is24Hour = true
    )


    val isPastTime = remember(state.hour, state.minute) {
        val pickedCal = Calendar.getInstance().apply {
            timeInMillis = selectedDateMillis
            set(Calendar.HOUR_OF_DAY, state.hour)
            set(Calendar.MINUTE, state.minute)
        }
        pickedCal.timeInMillis < System.currentTimeMillis()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(stringResource(R.string.select_time))
                if (isPastTime) {
                    Text(
                        "Cannot pick a time in the past",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
        text = { TimePicker(state = state) },
        confirmButton = {
            Button(
                onClick = {
                    val outMillis = Calendar.getInstance().apply {
                        timeInMillis = selectedDateMillis
                        set(Calendar.HOUR_OF_DAY, state.hour)
                        set(Calendar.MINUTE, state.minute)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }.timeInMillis
                    onTimeSelected(outMillis)
                    onDismiss()
                },
                enabled = !isPastTime // <--- DISABLE THE BUTTON
            ) {
                Text(stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
        }
    )
}