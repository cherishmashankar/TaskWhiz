package com.example.taskwhiz.presentation.ui.screen

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.taskwhiz.R
import com.example.taskwhiz.domain.model.Task
import com.example.taskwhiz.presentation.ui.components.*
import com.example.taskwhiz.presentation.ui.model.FilterItem
import com.example.taskwhiz.presentation.ui.theme.AppDimens
import com.example.taskwhiz.presentation.viewmodel.TaskViewModel
import com.example.taskwhiz.utils.isThisWeek


@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
@Composable
fun TaskListScreen(viewModel: TaskViewModel) {
    val tasks by viewModel.tasks.collectAsState(initial = emptyList())
    val visibleTasks by viewModel.visibleTasks.collectAsState(initial = emptyList())
    val activeFilters by viewModel.activeFilters.collectAsState(initial = emptySet())

    // If you have these in your VM already:
    val language by viewModel.language.collectAsState()
    val theme by viewModel.theme.collectAsState()

    var showSheet by remember { mutableStateOf(false) }
    var selectedTask: Task? by remember { mutableStateOf(null) }

    // ---- Dynamic counts for the grid
    val todayCount = tasks.count { it.dueAt?.let { d -> android.text.format.DateUtils.isToday(d) } == true }
    val thisWeekCount = tasks.count { it.dueAt?.let { d -> com.example.taskwhiz.utils.isThisWeek(d) } == true }
    val overdueCount = tasks.count { it.dueAt?.let { d -> d < System.currentTimeMillis() } == true }
    val reminderCount = tasks.count { it.reminderAt != null }
    val highPriorityCount = tasks.count { it.priorityLevel == 1 }

    val filterItems = listOf(
        FilterItem("Today", todayCount, Icons.Default.CalendarToday, MaterialTheme.colorScheme.primary),
        FilterItem("Overdue", overdueCount, Icons.Default.EventBusy, MaterialTheme.colorScheme.error),
        FilterItem("Reminder", reminderCount, Icons.Default.Notifications, MaterialTheme.colorScheme.primary),
        FilterItem("High Priority", highPriorityCount, Icons.Default.Star, MaterialTheme.colorScheme.tertiaryContainer)
    )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { selectedTask = null; showSheet = true },
                modifier = Modifier.padding(AppDimens.PaddingLarge),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_task_add),
                    contentDescription = stringResource(id = R.string.add_task),
                    modifier = Modifier.size(AppDimens.IconLarge + AppDimens.PaddingSmall)
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(AppDimens.PaddingXLarge)
        ) {
            // Search + Status chips
            item {
                Spacer(Modifier.height(AppDimens.PaddingXLarge))

                TaskSearchBar(
                    query = viewModel.search.collectAsState().value,
                    onQueryChange = { viewModel.search.value = it },
                    currentLanguage = language,
                    currentTheme = theme,
                    onLanguageChange = { viewModel.changeLanguage(it) },
                    onThemeChange = { viewModel.changeTheme(it) }
                )

                StatusFilterChips(
                    selectedStatus = viewModel.status.collectAsState().value,
                    onStatusSelected = { viewModel.status.value = it }
                )
            }

            // Filters grid (bounded height, parent LazyColumn scrolls)
            item {
                SectionTitle(text = stringResource(id = R.string.title_filters))
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 300.dp)
                        .padding(horizontal = AppDimens.PaddingLarge),
                    horizontalArrangement = Arrangement.spacedBy(AppDimens.PaddingLarge),
                    verticalArrangement = Arrangement.spacedBy(AppDimens.PaddingMedium),
                    userScrollEnabled = false
                ) {
                    items(filterItems.filter { it.count > 0 }) { filter ->
                        FilterCard(
                            item = filter,
                            isSelected = activeFilters.contains(filter.name),
                            onClick = { viewModel.toggleFilter(filter.name) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Tasks section (header + list in one visual group)
            item {
                Column(Modifier.fillMaxWidth()) {
                    SectionTitle(text = stringResource(id = R.string.title_tasks))
                    if (visibleTasks.isEmpty()) {
/*                        Text(
                            text = stringResource(id = R.string.no_tasks_found),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = AppDimens.PaddingLarge)
                        )*/
                    } else {
                        visibleTasks.forEach { task ->
                            TaskItem(
                                task = task,
                                onEditClick = { clickedTask ->
                                    selectedTask = clickedTask
                                    showSheet = true
                                },
                                onDeleteClick = { clickedTask -> viewModel.deleteTask(clickedTask) },
                                onToggle = { viewModel.toggleTaskCompletion(task) }
                            )
                            Spacer(Modifier.height(AppDimens.PaddingSmall))
                        }
                    }
                }
            }
        }
    }

    if (showSheet) {
        TaskEditorBottomSheet(
            task = selectedTask,
            onSave = { newTask -> viewModel.addNewTask(newTask); showSheet = false },
            onUpdate = { updatedTask -> viewModel.updateExistingTask(updatedTask); showSheet = false },
            onDismiss = { showSheet = false }
        )
    }
}
