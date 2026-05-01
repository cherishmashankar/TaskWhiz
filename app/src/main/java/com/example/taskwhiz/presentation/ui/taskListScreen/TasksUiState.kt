package com.example.taskwhiz.presentation.ui.taskListScreen

import com.example.taskwhiz.domain.model.Task

data class TasksUiState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)