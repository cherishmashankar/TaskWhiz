package com.example.taskwhiz.presentation.ui.taskCaptureScreen.componets


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskwhiz.domain.model.Task
import com.example.taskwhiz.presentation.ui.taskCaptureScreen.TaskCaptureEvent

@Composable
fun TaskPreviewSection(
    task: Task,
    onEvent: (TaskCaptureEvent) -> Unit
) {
    Column {

        Text(
            text = task.title,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (task.taskItems.isNotEmpty()) {
            task.taskItems.forEach {
                Text("• $it")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row {
            Button(
                onClick = {
                    onEvent(TaskCaptureEvent.OnSave)
                }
            ) {
                Text("Save")
            }

            Spacer(modifier = Modifier.width(12.dp))

            OutlinedButton(
                onClick = {
                    onEvent(TaskCaptureEvent.OnEdit)
                }
            ) {
                Text("Edit")
            }

            Spacer(modifier = Modifier.width(12.dp))

            TextButton(
                onClick = {
                    onEvent(TaskCaptureEvent.Reset)
                }
            ) {
                Text("Reset")
            }
        }
    }
}