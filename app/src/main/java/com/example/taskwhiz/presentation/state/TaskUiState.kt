package com.example.taskwhiz.presentation.state

import com.example.taskwhiz.domain.model.Task

data class TaskUiState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)