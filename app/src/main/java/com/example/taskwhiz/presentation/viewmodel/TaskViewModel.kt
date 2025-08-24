package com.example.taskwhiz.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskwhiz.domain.model.AppTheme
import com.example.taskwhiz.domain.model.Language
import com.example.taskwhiz.domain.model.Task
import com.example.taskwhiz.domain.usecase.DeleteTaskUseCase
import com.example.taskwhiz.domain.usecase.GetAllTasksUseCase
import com.example.taskwhiz.domain.usecase.GetLanguageUseCase
import com.example.taskwhiz.domain.usecase.GetTaskByIdUseCase
import com.example.taskwhiz.domain.usecase.GetThemeUseCase
import com.example.taskwhiz.domain.usecase.InsertTaskUseCase
import com.example.taskwhiz.domain.usecase.SetLanguageUseCase
import com.example.taskwhiz.domain.usecase.SetThemeUseCase
import com.example.taskwhiz.domain.usecase.UpdateTaskUseCase
import com.example.taskwhiz.presentation.filters.TaskUiFilterHelper
import com.example.taskwhiz.presentation.utils.TaskFilters
import com.example.taskwhiz.presentation.utils.TaskStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TaskViewModel @Inject constructor(
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val insertTaskUseCase: InsertTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val getTaskByIdUseCase: GetTaskByIdUseCase
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    val search = MutableStateFlow("")
    val status = MutableStateFlow(TaskStatus.ALL)

    private val _activeFilters = MutableStateFlow<Set<String>>(emptySet())
    val activeFilters: StateFlow<Set<String>> = _activeFilters


    init {
        viewModelScope.launch {
            getAllTasksUseCase().collect { _tasks.value = it }
        }
    }


    fun addNewTask(task: Task) = viewModelScope.launch { insertTaskUseCase(task) }

    fun updateExistingTask(task: Task) = viewModelScope.launch { updateTaskUseCase(task) }

    fun deleteTask(task: Task) = viewModelScope.launch {
        deleteTaskUseCase(task)
        getAllTasksUseCase().collect { _tasks.value = it }
    }

    fun toggleTaskCompletion(task: Task) =
        viewModelScope.launch { updateTaskUseCase(task.copy(isCompleted = !task.isCompleted)) }


    fun getTaskById(id: Long) = getTaskByIdUseCase(id)


    //Filters
    fun toggleFilter(selected: String) {
        val current = _activeFilters.value
        _activeFilters.value =
            if (selected in TaskFilters.DATE_FILTERS) {
                val currentDate = current intersect TaskFilters.DATE_FILTERS
                if (selected in currentDate) current - selected else (current - TaskFilters.DATE_FILTERS) + selected
            } else {
                if (selected in current) current - selected else current + selected
            }
    }

    val visibleTasks: StateFlow<List<Task>> =
        combine(tasks, activeFilters, search, status) { list, filters, q, st ->
            TaskUiFilterHelper.applyFilters(list, filters, q, st)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

}
