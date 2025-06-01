package com.example.taskwhiz.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskwhiz.domain.model.Task
import com.example.taskwhiz.domain.repository.TaskRepository
import com.example.taskwhiz.domain.usecase.GetAllTasksUseCase
import com.example.taskwhiz.domain.usecase.InsertRawNoteUseCase
import com.example.taskwhiz.domain.usecase.SyncMessyTasksUseCase

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val insertRawNoteUseCase: InsertRawNoteUseCase,
    private val syncMessyTasksUseCase: SyncMessyTasksUseCase,
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val taskRepository: TaskRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            taskRepository.insertTask(
                Task(
                    title = "Plan trip",
                    tag = "Personal",
                    createdAt = System.currentTimeMillis(),
                    isMessy = false,
                    taskItems = listOf("Book hotel", "Check flights"),
                    priorityLevel = 0
                )
            )
        }
    }

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    fun loadTasks() {
        viewModelScope.launch {
            _tasks.value = getAllTasksUseCase()
        }
    }

    fun addNote(rawInput: String, isOnline: Boolean) {
        viewModelScope.launch {
            if (isOnline) syncMessyTasksUseCase(System.currentTimeMillis())
            else insertRawNoteUseCase(rawInput, System.currentTimeMillis())

            loadTasks()
        }
    }

    fun addRawNote(rawText: String) {
        viewModelScope.launch {
            insertRawNoteUseCase(rawText, System.currentTimeMillis())
        }
    }
}