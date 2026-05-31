package com.example.taskwhiz.presentation.ui.taskListScreen

import android.Manifest
import android.app.Activity
import android.os.Build
import android.text.format.DateUtils
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.taskwhiz.R
import com.example.taskwhiz.navigation.Screen
import com.example.taskwhiz.presentation.helpers.shareTask
import com.example.taskwhiz.presentation.ui.taskListScreen.components.CenteredLoader
import com.example.taskwhiz.presentation.ui.taskListScreen.components.EmptyTasksScreen
import com.example.taskwhiz.presentation.ui.taskListScreen.components.ErrorScreen
import com.example.taskwhiz.presentation.utils.TaskFilters
import com.example.taskwhiz.presentation.ui.taskListScreen.components.SectionTitle
import com.example.taskwhiz.presentation.ui.taskListScreen.components.StatusFilterChips
import com.example.taskwhiz.presentation.ui.taskListScreen.components.TaskItem
import com.example.taskwhiz.presentation.ui.taskListScreen.components.TaskWhizFloatingActionButton
import com.example.taskwhiz.presentation.ui.theme.AppDimens
import com.example.taskwhiz.presentation.viewmodel.SettingsViewModel


@OptIn(ExperimentalMaterial3Api::class)
@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
@Composable
fun TaskListScreen(
    taskViewModel: TaskListViewModel = hiltViewModel() ,
//    settingsViewModel: SettingsViewModel = hiltViewModel(),
    navController: NavHostController,
    activity: Activity
) {
    val uiState by taskViewModel.uiState.collectAsStateWithLifecycle()


    val tasks by taskViewModel.tasks.collectAsState(initial = emptyList())
//    val activeFilters by taskViewModel.activeFilters.collectAsState(initial = emptySet())
//
//    val language by settingsViewModel.language.collectAsState()
//    val theme by settingsViewModel.theme.collectAsState()

    val todayCount =
        tasks.count { it.dueAt?.let { d -> DateUtils.isToday(d) } == true }
    val overdueCount = tasks.count { it.dueAt?.let { d -> d < System.currentTimeMillis() } == true }
    val reminderCount = tasks.count { it.reminderAt != null }
    val highPriorityCount = tasks.count { it.priorityLevel == 1 }

    val filterItems = listOf(
        FilterItem(
            TaskFilters.TODAY,
            R.string.filter_today,
            todayCount,
            Icons.Default.CalendarToday,
            MaterialTheme.colorScheme.primary
        ),
        FilterItem(
            TaskFilters.OVERDUE,
            R.string.filter_overdue,
            overdueCount,
            Icons.Default.EventBusy,
            MaterialTheme.colorScheme.error
        ),
        FilterItem(
            TaskFilters.REMINDER,
            R.string.filter_reminder,
            reminderCount,
            Icons.Default.Notifications,
            MaterialTheme.colorScheme.primary
        ),
        FilterItem(
            TaskFilters.HIGH_PRIORITY,
            R.string.filter_high_priority,
            highPriorityCount,
            Icons.Default.Star,
            MaterialTheme.colorScheme.tertiaryContainer
        )
    )

    Scaffold(
        floatingActionButton = {
            TaskWhizFloatingActionButton(
                onAddTaskClick = {
                    navController.navigate(Screen.TaskEditor.createRoute())
                },
                onSmartTaskClick = {
                    navController.navigate(Screen.TaskCapture.route)
                },
                modifier = Modifier.padding(AppDimens.PaddingLarge)
            )
        }
    ) { paddingValues ->

        when (val state = uiState) {
            TasksUiState.Loading -> {
                CenteredLoader()
            }
            TasksUiState.Empty -> {
                EmptyTasksScreen()
            }
            is TasksUiState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(MaterialTheme.colorScheme.background),
                    verticalArrangement = Arrangement.spacedBy(AppDimens.PaddingXLarge)
                ) {
                    item {
                        Spacer(Modifier.height(AppDimens.PaddingXLarge))
//                TaskSearchBar(
//                    query = taskViewModel.search.collectAsState().value,
//                    onQueryChange = { taskViewModel.search.value = it },
//                    currentLanguage = language,
//                    currentTheme = theme,
//                    onLanguageChange = { settingsViewModel.changeLanguage(it) },
//                    onThemeChange = { settingsViewModel.changeTheme(it) }
//                )
                        //SectionTitle(text = stringResource(id = R.string.title_filters))

                        StatusFilterChips(
                            selectedStatus = taskViewModel.status.collectAsState().value,
                            onStatusSelected = { taskViewModel.status.value = it }
                        )
                    }

//            item {
//                if (tasks.isNotEmpty()) {
//                    SectionTitle(text = stringResource(id = R.string.title_filters))
//                    LazyVerticalGrid(
//                        columns = GridCells.Fixed(3),
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .heightIn(max = 300.dp)
//                            .padding(horizontal = AppDimens.PaddingLarge),
//                        horizontalArrangement = Arrangement.spacedBy(AppDimens.PaddingLarge),
//                        verticalArrangement = Arrangement.spacedBy(AppDimens.PaddingMedium),
//                        userScrollEnabled = false
//                    ) {
//                        items(filterItems.filter { it.count > 0 }) { filter ->
//                            FilterCard(
//                                item = filter,
//                                isSelected = activeFilters.contains(filter.key),
//                                onClick = { taskViewModel.toggleFilter(filter.key) },
//                                modifier = Modifier.fillMaxWidth()
//                            )
//                        }
//                    }
//                }
//            }

                    item {
                        Column(Modifier.fillMaxWidth()) {
                            SectionTitle(text = stringResource(id = R.string.title_tasks))
                            state.tasks.forEach { task ->
                                TaskItem(
                                    task = task,
                                    onEditClick = { clickedTask ->
                                        navController.navigate(Screen.TaskEditor.createRoute(clickedTask.id))
                                    },
                                    onDeleteClick = { clickedTask ->
                                        taskViewModel.deleteTask(clickedTask)
                                    },
                                    onShareClick = { clickedTask ->
                                        shareTask(activity, clickedTask)
                                        Log.d("ShareTask", "Launching chooser for task share $activity")
                                    },
                                    onToggle = { taskViewModel.toggleTaskCompletion(task) }
                                )
                                Spacer(Modifier.height(AppDimens.PaddingSmall))
                            }
                        }
                    }
                }
            }

            is TasksUiState.Error -> {
                ErrorScreen( message = state.message)
            }
        }
    }
}

