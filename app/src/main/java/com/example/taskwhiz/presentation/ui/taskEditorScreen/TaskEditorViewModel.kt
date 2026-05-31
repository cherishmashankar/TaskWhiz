package com.example.taskwhiz.presentation.ui.taskEditorScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskwhiz.domain.model.Task
import com.example.taskwhiz.domain.usecase.reminder.SyncTaskReminderUseCase
import com.example.taskwhiz.domain.usecase.task.DeleteTaskUseCase
import com.example.taskwhiz.domain.usecase.task.GenerateTaskFromAIUseCase
import com.example.taskwhiz.domain.usecase.task.GetTaskByIdUseCase
import com.example.taskwhiz.domain.usecase.task.InsertTaskUseCase
import com.example.taskwhiz.domain.usecase.task.UpdateTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TaskEditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val insertTaskUseCase: InsertTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    getTaskByIdUseCase: GetTaskByIdUseCase,
    private val syncReminder: SyncTaskReminderUseCase,
) : ViewModel() {

    private val taskId: Long = savedStateHandle["taskId"] ?: -1L

    val task: StateFlow<Task?> =
        if (taskId == -1L) {
            MutableStateFlow(null)
        } else {
            getTaskByIdUseCase(taskId)
                .stateIn(
                    viewModelScope,
                    SharingStarted.WhileSubscribed(5_000),
                    null
                )
        }

    fun saveTask(task: Task) {
        viewModelScope.launch {
            if (task.id == 0L) {
                val generatedId = insertTaskUseCase(task)

                syncReminder(task.copy(id = generatedId))
            } else {
                updateTaskUseCase(task)
                syncReminder(task)
            }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            syncReminder(task.copy(reminderAt = null))
            deleteTaskUseCase(task)
        }
    }
}
