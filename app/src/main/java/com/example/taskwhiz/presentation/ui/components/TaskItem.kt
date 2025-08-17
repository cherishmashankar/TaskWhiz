package com.example.taskwhiz.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskwhiz.domain.model.Task
import com.example.taskwhiz.utils.toFullDateTime

@Composable
fun TaskItem(
    task: Task,
    modifier: Modifier = Modifier,
    onEditClick: (Task) -> Unit,
    onDeleteClick: (Task) -> Unit,
    onToggle: () -> Unit,
) {
    val formattedTime = remember(task.lastModifiedAt) {
        task.lastModifiedAt.toFullDateTime()
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CompletionToggle(
                    isCompleted = task.isCompleted,
                    colorHex = task.colorCode,
                    onToggle = onToggle
                )
                Spacer(Modifier.width(16.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = formattedTime,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            TaskStatusBadges(
                dueDate = task.dueAt,
                reminderDate = task.reminderAt,
                isHighPriority = (task.priorityLevel == 1),
            )
        }

        TaskOverflowMenu(
            onEditClick = { onEditClick(task) },
            onDeleteClick = { onDeleteClick(task) }
        )
    }
}






