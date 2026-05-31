package com.example.taskwhiz.presentation.ui.taskCaptureScreen

import com.example.taskwhiz.domain.model.Task

sealed interface TaskCaptureUiState {

    data object Idle : TaskCaptureUiState

    data object Loading : TaskCaptureUiState

    data class Preview(
        val task: Task
    ) : TaskCaptureUiState

    data class Error(
        val message: String
    ) : TaskCaptureUiState
}