package com.example.taskwhiz.navigation

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue


import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.taskwhiz.domain.model.Task
import com.example.taskwhiz.presentation.viewmodel.TaskViewModel
import com.example.taskwhiz.presentation.ui.taskListScreen.TaskListScreen
import com.example.taskwhiz.presentation.ui.taskEditorScreen.TaskEditorScreen


@Composable
fun AppNavGraph(
    navController: NavHostController,
    activity: Activity
) {
    NavHost(
        navController = navController,
        startDestination = Screen.TaskList.route
    ) {
        composable(Screen.TaskList.route) {
            TaskListScreen(
                navController = navController,
                activity = activity
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
        ) {
            TaskEditorScreen(
                onBack = { navController.popBackStack() }
            )
        }

    }
}