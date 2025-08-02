package com.example.taskwhiz.presentation.ui.components



import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskwhiz.domain.model.Task
import com.example.taskwhiz.presentation.ui.theme.TaskWhizTheme
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.TextButton
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import java.text.DateFormat
import java.util.*
import androidx.core.graphics.toColorInt

@Composable
fun TaskItem(
    task: Task,
    modifier: Modifier = Modifier,
    onEditClick: (Task) -> Unit,
    onDeleteClick: (Task) -> Unit,
    onToggle: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            // Top Row: Completion + Title + Expand
            Column(modifier = Modifier.padding(16.dp)) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    CompletionToggle(
                        isCompleted = task.isCompleted,
                        colorHex = task.colorCode,
                        onToggle = onToggle
                    )

                    Spacer(Modifier.width(12.dp))

                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 20.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = "Expand",
                        modifier = Modifier
                            .size(28.dp)
                            .clickable { expanded = !expanded }
                    )
                }

                Spacer(Modifier.height(8.dp))

                // Meta Info Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Updated: ${DateFormat.getDateTimeInstance().format(Date(task.lastModifiedAt))}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )

                    task.dueAt?.let {
                        Text(
                            text = "Due: ${DateFormat.getDateTimeInstance().format(Date(it))}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (it < System.currentTimeMillis()) Color.Red else Color.Gray
                        )
                    }

                    task.reminderAt?.let {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Reminder",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = DateFormat.getDateTimeInstance().format(Date(it)),
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }

            // Expanded Content
            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    // Subtasks
                    if (task.taskItems.isNotEmpty()) {
                        task.taskItems.forEach {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(bottom = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.FiberManualRecord,
                                    contentDescription = null,
                                    tint = Color.Gray,
                                    modifier = Modifier
                                        .size(16.dp)
                                        .padding(end = 4.dp)
                                )
                                Text(
                                    text = it,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                    }

                    // Action Buttons
                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        TextButton(onClick = { onEditClick(task) }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Note",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = "Edit",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        TextButton(onClick = { onDeleteClick(task) }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Note",
                                tint = Color.Red,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = "Delete",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.Red
                            )
                        }
                    }
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun TaskItemPreview() {
    TaskWhizTheme {
        TaskItem(
            task = Task(
                id = 1,
                title = "Prepare presentation",
                tag = "Work",
                createdAt = System.currentTimeMillis() - 1000000,
                lastModifiedAt = System.currentTimeMillis(),
                dueAt = null,
                reminderAt = System.currentTimeMillis() + 3600000,
                taskItems = listOf("Add charts", "Rehearse", "Send invite"),
                isMessy = false,
                rawInput = null,
                isCompleted = false,
                colorCode = "#FF9300",
                priorityLevel = 0,
                archived = false
            ),
            modifier = TODO(),
            onEditClick = TODO(),
            onDeleteClick = TODO(),
            onToggle = TODO(),
        )
    }
}
