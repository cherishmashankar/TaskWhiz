package com.example.taskwhiz.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskwhiz.domain.model.Task
import com.example.taskwhiz.domain.repository.TaskRepository
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
    private val insertRawNoteUseCase: InsertRawNoteUseCase,
    private val syncMessyTasksUseCase: SyncMessyTasksUseCase,
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val taskRepository: TaskRepository,
    private val insertTaskUseCase: InsertTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase
) : ViewModel() {


    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    fun loadTasks() {
        viewModelScope.launch {
            _tasks.value = getAllTasksUseCase()
        }
    }

    fun addNewTask(
        title: String,
        colorCode: String,
        subtasks: List<String>
    ) {
        viewModelScope.launch {
            val currentTime = System.currentTimeMillis()

            val newTask = Task(
                title = title,
                tag = "",
                createdAt = currentTime,
                isMessy = true, // mark messy initially (parsed later)
                taskItems = subtasks,
                priorityLevel = 0,
                colorCode = colorCode
            )

            insertTaskUseCase(newTask)
            loadTasks()
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


}