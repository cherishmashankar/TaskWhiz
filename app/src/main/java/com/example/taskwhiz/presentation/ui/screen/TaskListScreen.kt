package com.example.taskwhiz.presentation.ui.screen

import android.Manifest
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.taskwhiz.presentation.ui.components.RawInputDialog
import com.example.taskwhiz.presentation.ui.components.TaskItem
import com.example.taskwhiz.presentation.util.NetworkUtils
import com.example.taskwhiz.presentation.viewmodel.TaskViewModel

@Composable
@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
fun TaskListScreen(viewModel: TaskViewModel) {
    val tasks by viewModel.tasks.collectAsState()
    val context = LocalContext.current
    val isOnline = remember { mutableStateOf(NetworkUtils.isOnline(context)) }

    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { viewModel.loadTasks() }

    Scaffold(
        floatingActionButton = {
            Box(modifier = Modifier.fillMaxSize()) {
                // Task list here...

                FloatingActionButton(
                    onClick = { showDialog = true },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }

                RawInputDialog(
                    showDialog = showDialog,
                    onDismiss = { showDialog = false },
                    onSubmit = { rawText ->
                        viewModel.addRawNote(rawText)
                    }
                )
        }}
    ) {
        LazyColumn(modifier = Modifier.padding(it)) {
            items(tasks) { task -> TaskItem(task = task) }
        }
    }
}
