package com.example.taskwhiz.presentation.ui.screen


import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import com.example.taskwhiz.domain.model.Task


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



// Scaffold for task list
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // For adding, ensure selectedTask is null
                    selectedTask = null
                    showSheet = true
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(tasks) { task ->
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
    }

// Bottom Sheet for Add / Edit Task
    if (showSheet) {
        TaskEditorBottomSheet(
            task = selectedTask, // null → add, not null → edit
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
