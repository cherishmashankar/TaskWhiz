package com.example.taskwhiz.presentation.ui.components



import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskwhiz.domain.model.Task
import com.example.taskwhiz.presentation.ui.theme.TaskWhizTheme
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import java.text.DateFormat
import java.util.*
import androidx.core.graphics.toColorInt


@Composable
fun TaskItem(
    task: Task,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background // <- background color from theme
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {

            Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max)) {


                Box(
                    modifier = Modifier
                        .width(16.dp)
                        .fillMaxHeight()
                        .background(Color(task.colorCode.toColorInt()))
                )

                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {


                        Spacer(modifier = Modifier.width(8.dp))

                        // Completion icon
                        Icon(
                            imageVector = if (task.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                            contentDescription = null,
                            tint = if (task.isCompleted) Color(0xFF4CAF50) else Color.Gray,
                            modifier = Modifier.size(18.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        // Title
                        Text(
                            text = task.title,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )

                        // Expand/Collapse icon
                        Icon(
                            imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = "Expand",
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Meta info
                    Text(
                        text = "Updated: ${
                            DateFormat.getDateTimeInstance().format(Date(task.lastModifiedAt))
                        }",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )

                    task.dueAt?.let {
                        Text(
                            text = "Due: ${DateFormat.getDateTimeInstance().format(Date(it))}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }

                    task.reminderAt?.let {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Reminder",
                                tint = Color(0xFF0A0A0A),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = DateFormat.getDateTimeInstance().format(Date(it)),
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }
                    }

                    // Subtasks (only if expanded)
                    AnimatedVisibility(visible = expanded) {
                        Column {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Subtasks:",
                                style = MaterialTheme.typography.labelMedium
                            )
                            Spacer(modifier = Modifier.height(4.dp))

                            task.taskItems.forEach {
                                Text(
                                    text = "• $it",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(start = 8.dp, bottom = 2.dp)
                                )
                            }
                        }
                    }
                }


            Spacer(modifier = Modifier.height(12.dp))
        }
    }}
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
            )
        )
    }
}
