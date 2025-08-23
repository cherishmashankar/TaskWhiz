package com.example.taskwhiz.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.taskwhiz.domain.model.Task
import com.example.taskwhiz.presentation.viewmodel.TaskViewModel
import com.example.taskwhiz.presentation.ui.screen.taskList.TaskListScreen
import com.example.taskwhiz.presentation.ui.screen.taskEditor.TaskEditorScreen

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun AppNavGraph(
    navController: NavHostController,
    onSaveTask: (Task) -> Unit,
    onUpdateTask: (Task) -> Unit,
    taskViewModel: TaskViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.TaskList.route
    ) {
        // Task List Screen
        composable(Screen.TaskList.route) {
            TaskListScreen(
                viewModel = taskViewModel,
                navController = navController
            )
        }

        composable(
            route = Screen.TaskEditor.route,
            arguments = listOf(
                navArgument("taskId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getLong("taskId") ?: -1L

            // If editing → collect task, else just null
            val task by taskViewModel
                .getTaskById(taskId)
                .collectAsState(initial = null)

            TaskEditorScreen(
                task = if (taskId == -1L) null else task,
                onSave = {
                    onSaveTask(it)
                    navController.popBackStack()
                },
                onDismiss = { navController.popBackStack() },
                onUpdate = {
                    onUpdateTask(it)
                    navController.popBackStack()
                }
            )
        }
    }
}