package com.example.taskwhiz.navigation


sealed class Screen(val route: String) {
    data object TaskList : Screen("task_list")

    data object TaskEditor : Screen("task_editor/{taskId}") {
        fun createRoute(taskId: Long = -1L): String {
            return "task_editor/$taskId"
        }
    }
}