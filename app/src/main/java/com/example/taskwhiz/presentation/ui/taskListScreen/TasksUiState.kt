package com.example.taskwhiz.presentation.ui.taskListScreen

import com.example.taskwhiz.domain.model.Task

sealed interface TasksUiState {
    data object Loading : TasksUiState
    data object Empty : TasksUiState
    data class Success(val tasks: List<Task>) : TasksUiState
    data class Error(val message: String) : TasksUiState
}