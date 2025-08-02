package com.example.taskwhiz.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskwhiz.domain.model.Task
import com.example.taskwhiz.domain.repository.TaskRepository
import com.example.taskwhiz.domain.usecase.DeleteTaskUseCase
import com.example.taskwhiz.domain.usecase.GetAllTasksUseCase
import com.example.taskwhiz.domain.usecase.InsertRawNoteUseCase
import com.example.taskwhiz.domain.usecase.InsertTaskUseCase
import com.example.taskwhiz.domain.usecase.SyncMessyTasksUseCase
import com.example.taskwhiz.domain.usecase.UpdateTaskUseCase

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val insertTaskUseCase: InsertTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    init {
        viewModelScope.launch {
            getAllTasksUseCase().collect { taskList ->
                _tasks.value = taskList
            }
        }
    }
    fun updateExistingTask(task: Task) {
        viewModelScope.launch {
            updateTaskUseCase(task)
        }
    }

    fun addNewTask(task: Task) {
        viewModelScope.launch {
            insertTaskUseCase(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            deleteTaskUseCase(task)
        }
    }

    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            val updatedTask = task.copy(isCompleted = !task.isCompleted)
            updateTaskUseCase(updatedTask)
        }
    }




}