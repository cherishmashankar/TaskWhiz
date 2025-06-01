package com.example.taskwhiz.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskwhiz.domain.model.Task

import java.text.DateFormat
import java.util.Date

@Composable
fun TaskItem(task: Task) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp)) {

        Text(
            text = task.title,
            style = MaterialTheme.typography.titleMedium
        )

        task.dueAt?.let {
            Text(
                text = "Due: ${DateFormat.getDateTimeInstance().format(Date(it))}",
                style = MaterialTheme.typography.bodySmall
            )
        }

        if (task.taskItems.isNotEmpty()) {
            Text(
                text = "Subtasks: ${task.taskItems.joinToString(", ")}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}