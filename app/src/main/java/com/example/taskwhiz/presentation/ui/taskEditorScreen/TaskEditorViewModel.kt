package com.example.taskwhiz.presentation.ui.taskEditorScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskwhiz.domain.model.Task
import com.example.taskwhiz.domain.usecase.task.DeleteTaskUseCase
import com.example.taskwhiz.domain.usecase.task.GetAllTasksUseCase
import com.example.taskwhiz.domain.usecase.task.GetTaskByIdUseCase
import com.example.taskwhiz.domain.usecase.task.InsertTaskUseCase
import com.example.taskwhiz.domain.usecase.task.UpdateTaskUseCase
import com.example.taskwhiz.presentation.helpers.TaskUiFilterHelper
import com.example.taskwhiz.presentation.utils.TaskFilters
import com.example.taskwhiz.presentation.utils.TaskStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class TaskEditorViewModel @Inject constructor(
    private val insertTaskUseCase: InsertTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
) : ViewModel() {

    private val _task = MutableStateFlow<List<Task>>(emptyList())
    val task: StateFlow<List<Task>> = _task



    fun addNewTask(task: Task) = viewModelScope.launch {
        insertTaskUseCase(task)
    }

    fun updateExistingTask(task: Task) = viewModelScope.launch {
        updateTaskUseCase(task)
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        deleteTaskUseCase(task)
    }

    fun getTaskById(id: Long) = getTaskByIdUseCase(id)


}
