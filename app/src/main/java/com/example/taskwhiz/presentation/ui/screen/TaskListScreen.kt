package com.example.taskwhiz.presentation.ui.screen


import android.Manifest
import android.os.Build
import android.text.format.DateUtils
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.taskwhiz.presentation.ui.components.TaskEditorBottomSheet
import com.example.taskwhiz.presentation.ui.components.TaskItem
import com.example.taskwhiz.presentation.util.NetworkUtils
import com.example.taskwhiz.presentation.viewmodel.TaskViewModel
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.example.taskwhiz.domain.model.Task
import java.util.Calendar


@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@OptIn(ExperimentalMaterial3Api::class)
@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
@Composable
fun TaskListScreen(viewModel: TaskViewModel) {
    val tasks by viewModel.tasks.collectAsState()
    val context = LocalContext.current
    val isOnline = remember { mutableStateOf(NetworkUtils.isOnline(context)) }
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
                modifier = Modifier.padding(16.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary

            ) {
                Icon(Icons.Default.Add,
                    contentDescription = "Add",
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

            Spacer(Modifier.height(32.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(25),
                elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search tasks") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    },
                    singleLine = true,
/*                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent
                    )*/
                )
            }


            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(statusFilters) { filter ->
                    FilterChip(
                        selected = selectedStatus == filter,
                        onClick = { selectedStatus = filter },
                        label = { Text(filter) }
                    )
                }
            }

/*            // Date Filter Chips
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(dateFilters) { filter ->
                    FilterChip(
                        selected = selectedDateFilter == filter,
                        onClick = { selectedDateFilter = filter },
                        label = { Text(filter) }
                    )
                }
            }*/

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


                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
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

                    }
                }

            Spacer(Modifier.height(16.dp))
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

