package com.example.taskwhiz.presentation.ui.screen.taskListScreen

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.taskwhiz.R
import com.example.taskwhiz.navigation.Screen
import com.example.taskwhiz.presentation.sharetask.shareTask
import com.example.taskwhiz.presentation.ui.model.FilterItem
import com.example.taskwhiz.presentation.utils.TaskFilters
import com.example.taskwhiz.presentation.ui.screen.taskListScreen.components.FilterCard
import com.example.taskwhiz.presentation.ui.screen.taskListScreen.components.SectionTitle
import com.example.taskwhiz.presentation.ui.screen.taskListScreen.components.StatusFilterChips
import com.example.taskwhiz.presentation.ui.screen.taskListScreen.components.TaskItem
import com.example.taskwhiz.presentation.ui.screen.taskListScreen.components.TaskSearchBar
import com.example.taskwhiz.presentation.ui.theme.AppDimens
import com.example.taskwhiz.presentation.viewmodel.SettingsViewModel
import com.example.taskwhiz.presentation.viewmodel.TaskViewModel

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
@Composable
fun TaskListScreen(
    taskViewModel: TaskViewModel = hiltViewModel() ,
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    navController: NavHostController,
    activity: Activity
) {


    val tasks by taskViewModel.tasks.collectAsState(initial = emptyList())
    val visibleTasks by taskViewModel.visibleTasks.collectAsState(initial = emptyList())
    val activeFilters by taskViewModel.activeFilters.collectAsState(initial = emptySet())

    val language by settingsViewModel.language.collectAsState()
    val theme by settingsViewModel.theme.collectAsState()

    val todayCount =
        tasks.count { it.dueAt?.let { d -> android.text.format.DateUtils.isToday(d) } == true }
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
            FloatingActionButton(
                onClick = { navController.navigate(Screen.TaskEditor.createRoute()) },
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

            item {

                Spacer(Modifier.height(AppDimens.PaddingXLarge))
                TaskSearchBar(
                    query = taskViewModel.search.collectAsState().value,
                    onQueryChange = { taskViewModel.search.value = it },
                    currentLanguage = language,
                    currentTheme = theme,
                    onLanguageChange = { settingsViewModel.changeLanguage(it) },
                    onThemeChange = { settingsViewModel.changeTheme(it) }
                )

                StatusFilterChips(
                    selectedStatus = taskViewModel.status.collectAsState().value.toString(),
                    onStatusSelected = { taskViewModel.status.value = it }
                )
            }

            item {
                if (tasks.isNotEmpty()) {
                    SectionTitle(text = stringResource(id = R.string.title_filters))
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
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
                                isSelected = activeFilters.contains(filter.key),
                                onClick = { taskViewModel.toggleFilter(filter.key) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

            item {
                Column(Modifier.fillMaxWidth()) {
                    SectionTitle(text = stringResource(id = R.string.title_tasks))
                    if (visibleTasks.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(AppDimens.PaddingXLarge),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Inbox, // or a custom illustration
                                contentDescription = null,
                                modifier = Modifier
                                    .size(AppDimens.IconXLarge * 2) // 64dp
                                    .padding(bottom = AppDimens.PaddingMedium),
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                            )
                            Text(
                                text = stringResource(R.string.no_tasks),
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(AppDimens.PaddingSmall))
                            Text(
                                text = stringResource(R.string.no_tasks_hint),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = AppDimens.PaddingLarge)
                            )
                        }
                    } else {
                        visibleTasks.forEach { task ->
                            TaskItem(
                                task = task,
                                onEditClick = { clickedTask ->
                                    navController.navigate(Screen.TaskEditor.createRoute(clickedTask.id))
                                },
                                onDeleteClick = { clickedTask ->
                                    taskViewModel.deleteTask(clickedTask)
                                },
                                onShareClick = {clickedTask ->
                                    activity?.let { shareTask(it, clickedTask) }
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

    }
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
