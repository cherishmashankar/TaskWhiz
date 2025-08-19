package com.example.taskwhiz.presentation.ui.screen

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource

import com.example.taskwhiz.R
import com.example.taskwhiz.domain.model.Task
import com.example.taskwhiz.presentation.ui.components.TaskEditorBottomSheet
import com.example.taskwhiz.presentation.ui.components.TaskItem
import com.example.taskwhiz.presentation.ui.theme.AppDimens
import com.example.taskwhiz.presentation.viewmodel.TaskViewModel

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
@Composable
fun TaskListScreen(viewModel: TaskViewModel) {
    val tasks by viewModel.tasks.collectAsState()

    var showSheet by remember { mutableStateOf(false) }
    var selectedTask: Task? by remember { mutableStateOf(null) }

    var searchQuery by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf("All") }
    var selectedDateFilter by remember { mutableStateOf("All Dates") }

    val statusFilters = listOf("All", "Completed", "Pending")
    val dateFilters = listOf("All Dates", "Today", "This Week", "Overdue")

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    selectedTask = null
                    showSheet = true
                },
                modifier = Modifier.padding(AppDimens.PaddingLarge),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_task_add),
                    contentDescription = "Add Task",
                    modifier = Modifier.size(AppDimens.IconLarge + AppDimens.PaddingSmall) // 28dp
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Spacer(Modifier.height(AppDimens.PaddingXLarge + AppDimens.PaddingLarge)) // 32dp

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppDimens.PaddingLarge, vertical = AppDimens.PaddingSmall),
                shape = RoundedCornerShape(AppDimens.CornerLarge),
                elevation = CardDefaults.cardElevation(defaultElevation = AppDimens.PaddingLarge), // 16dp
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            "Search tasks",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                        errorTextColor = MaterialTheme.colorScheme.error,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.primary,
                        errorCursorColor = MaterialTheme.colorScheme.error
                    )
                )
            }

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppDimens.PaddingLarge, vertical = AppDimens.PaddingXSmall),
                horizontalArrangement = Arrangement.spacedBy(AppDimens.PaddingSmall)
            ) {
                items(statusFilters.size) { i ->
                    val filter = statusFilters[i]
                    FilterChip(
                        selected = selectedStatus == filter,
                        onClick = { selectedStatus = filter },
                        label = { Text(filter) }
                    )
                }
            }

            // --- Optional Date Filter ---
            /*
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppDimens.PaddingLarge, vertical = AppDimens.PaddingXSmall),
                horizontalArrangement = Arrangement.spacedBy(AppDimens.PaddingSmall)
            ) {
                items(dateFilters.size) { i ->
                    val filter = dateFilters[i]
                    FilterChip(
                        selected = selectedDateFilter == filter,
                        onClick = { selectedDateFilter = filter },
                        label = { Text(filter) }
                    )
                }
            }
            */

            // Filter + Search Logic
            val filteredTasks = tasks.filter { task ->
                val matchesSearch = task.title.contains(searchQuery, ignoreCase = true)

                val matchesStatus = when (selectedStatus) {
                    "Completed" -> task.isCompleted
                    "Pending" -> !task.isCompleted
                    else -> true
                }

                val matchesDate = when (selectedDateFilter) {
                    "Today" -> task.dueAt?.let { android.text.format.DateUtils.isToday(it) } ?: false
                    "This Week" -> task.dueAt?.let { com.example.taskwhiz.utils.isThisWeek(it) } ?: false
                    "Overdue" -> task.dueAt?.let { it < System.currentTimeMillis() } ?: false
                    else -> true
                }

                matchesSearch && matchesStatus && matchesDate
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppDimens.PaddingLarge, vertical = AppDimens.PaddingSmall),
                shape = RoundedCornerShape(AppDimens.CornerMedium),
                elevation = CardDefaults.cardElevation(defaultElevation = AppDimens.PaddingSmall),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    itemsIndexed(filteredTasks) { index, task ->
                        TaskItem(
                            task = task,
                            onEditClick = { clickedTask ->
                                selectedTask = clickedTask
                                showSheet = true
                            },
                            onDeleteClick = { clickedTask ->
                                viewModel.deleteTask(clickedTask)
                            },
                            onToggle = {
                                viewModel.toggleTaskCompletion(task)
                            }
                        )

                        if (index < filteredTasks.lastIndex) {
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                                modifier = Modifier.padding(horizontal = AppDimens.PaddingLarge)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(AppDimens.PaddingXLarge + AppDimens.PaddingLarge)) // 32dp
        }
    }

    if (showSheet) {
        TaskEditorBottomSheet(
            task = selectedTask,
            onSave = { newTask ->
                viewModel.addNewTask(newTask)
                showSheet = false
            },
            onUpdate = { updatedTask ->
                viewModel.updateExistingTask(updatedTask)
                showSheet = false
            },
            onDismiss = { showSheet = false }
        )
    }
}
