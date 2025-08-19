package com.example.taskwhiz.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import com.example.taskwhiz.domain.model.Task
import com.example.taskwhiz.presentation.ui.theme.AppDimens
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
    val showBadges = task.dueAt != null || task.reminderAt != null || task.priorityLevel == 1

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = AppDimens.PaddingLarge,
                vertical = AppDimens.TaskItemPaddingVertical
            ),
    ) {
        Column(Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CompletionToggle(
                    isCompleted = task.isCompleted,
                    colorHex = task.colorCode,
                    onToggle = onToggle
                )
                Spacer(Modifier.width(AppDimens.PaddingLarge))
                Column(Modifier.weight(1f)) {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            textDecoration = TextDecoration.None,
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(AppDimens.TaskTitleTimestampSpacing))
                    Text(
                        text = formattedTime,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            if (showBadges) {
                Spacer(Modifier.height(AppDimens.TaskBadgesSpacing))
                TaskStatusBadges(
                    dueDate = task.dueAt,
                    reminderDate = task.reminderAt,
                    isHighPriority = (task.priorityLevel == 1),
                )
            }
        }

        TaskOverflowMenu(
            onEditClick = { onEditClick(task) },
            onDeleteClick = { onDeleteClick(task) }
        )
    }
}




