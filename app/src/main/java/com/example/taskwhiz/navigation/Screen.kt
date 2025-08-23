package com.example.taskwhiz.navigation


sealed class Screen(val route: String) {
    object TaskList : Screen("task_list")

    object TaskEditor : Screen("task_editor/{taskId}") {
        fun createRoute(taskId: Long? = null): String {
            return if (taskId == null) {
                "task_editor/-1" // -1 means "new task"
            } else {
                "task_editor/$taskId"
            }
        }
    }
}