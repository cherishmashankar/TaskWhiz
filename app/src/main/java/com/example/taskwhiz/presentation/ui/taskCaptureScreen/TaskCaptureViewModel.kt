package com.example.taskwhiz.presentation.ui.taskCaptureScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskwhiz.domain.model.Task
import com.example.taskwhiz.domain.repository.TaskRepository
import com.example.taskwhiz.domain.usecase.task.GenerateTaskFromAIUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TaskCaptureViewModel @Inject constructor(
    private val generateTaskFromAIUseCase: GenerateTaskFromAIUseCase,
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<TaskCaptureUiState>(
        TaskCaptureUiState.Idle
    )
    val uiState: StateFlow<TaskCaptureUiState> = _uiState

    private var currentTask: Task? = null

    fun onEvent(event: TaskCaptureEvent) {
        when (event) {

            is TaskCaptureEvent.OnSubmit -> {
                generateTask(event.text)
            }

            TaskCaptureEvent.OnSave -> {
                saveTask()
            }

            TaskCaptureEvent.Reset -> {
                resetState()
            }

            TaskCaptureEvent.OnEdit -> {
                _uiState.value = TaskCaptureUiState.Idle
                currentTask = null
            }
        }
    }

    private fun generateTask(text: String) {
        viewModelScope.launch {
            _uiState.value = TaskCaptureUiState.Loading

            try {
                val task = generateTaskFromAIUseCase(text)
                currentTask = task
                _uiState.value = TaskCaptureUiState.Preview(task)

            } catch (e: Exception) {
                _uiState.value = TaskCaptureUiState.Error(
                    e.message ?: "Something went wrong"
                )
            }
        }
    }

    private fun saveTask() {
        viewModelScope.launch {
            val task = currentTask ?: return@launch

            try {
                taskRepository.insertTask(task)
                resetState()

            } catch (e: Exception) {
                _uiState.value = TaskCaptureUiState.Error(
                    e.message ?: "Failed to save task"
                )
            }
        }
    }

    private fun resetState() {
        currentTask = null
        _uiState.value = TaskCaptureUiState.Idle
    }
}