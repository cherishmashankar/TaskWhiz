package com.example.taskwhiz.presentation.helpers

import android.text.format.DateUtils
import com.example.taskwhiz.domain.model.Task
import com.example.taskwhiz.presentation.utils.TaskFilters
import com.example.taskwhiz.presentation.utils.TaskStatus

object TaskUiFilterHelper {
    fun applyFilters(
        tasks: List<Task>,
        filters: Set<String>,
        search: String,
        status: String
    ): List<Task> {
        return tasks.filter { task ->
            val matchesSearch = task.title.contains(search, ignoreCase = true)

            val matchesStatus = when (status) {
                TaskStatus.COMPLETED -> task.isCompleted
                TaskStatus.PENDING -> !task.isCompleted
                else -> true
            }

            val dateMatch = when {
                TaskFilters.TODAY in filters -> task.dueAt?.let { DateUtils.isToday(it) } == true
                TaskFilters.OVERDUE in filters -> task.dueAt?.let { it < System.currentTimeMillis() } == true
                else -> true
            }

            val reminderMatch = if (TaskFilters.REMINDER in filters) task.reminderAt != null else true
            val priorityMatch = if (TaskFilters.HIGH_PRIORITY in filters) task.priorityLevel == 1 else true

            matchesSearch && matchesStatus && dateMatch && reminderMatch && priorityMatch
        }
    }
}