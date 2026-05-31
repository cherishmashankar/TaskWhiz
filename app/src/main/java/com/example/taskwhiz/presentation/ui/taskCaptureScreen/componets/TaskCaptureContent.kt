package com.example.taskwhiz.presentation.ui.taskCaptureScreen.componets


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskwhiz.presentation.ui.taskCaptureScreen.TaskCaptureEvent
import com.example.taskwhiz.presentation.ui.taskCaptureScreen.TaskCaptureUiState

@Composable
fun TaskCaptureContent(
    uiState: TaskCaptureUiState,
    input: String,
    onEvent: (TaskCaptureEvent) -> Unit
) {
    when (uiState) {

        is TaskCaptureUiState.Idle -> {
            // nothing extra
        }

        is TaskCaptureUiState.Loading -> {
            LoadingState()
        }

        is TaskCaptureUiState.Preview -> {
            TaskPreviewSection(
                task = uiState.task,
                onEvent = onEvent
            )
        }

        is TaskCaptureUiState.Error -> {
            ErrorSection(
                message = uiState.message,
                onRetry = {
                    onEvent(TaskCaptureEvent.OnSubmit(input))
                }
            )
        }
    }
}