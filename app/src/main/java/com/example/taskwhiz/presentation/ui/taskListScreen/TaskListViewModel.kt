package com.example.taskwhiz.presentation.ui.taskListScreen

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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
) : ViewModel() {
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    val search = MutableStateFlow("")
    val status = MutableStateFlow(TaskStatus.ALL)

    private val _activeFilters = MutableStateFlow<Set<String>>(emptySet())
    val activeFilters: StateFlow<Set<String>> = _activeFilters

    private val tasksFlow = getAllTasksUseCase()
        .catch { exception ->
            emit(emptyList())
        }

    val visibleTasks: StateFlow<List<Task>> =
        combine(tasksFlow, activeFilters, search, status) { list, filters, q, st ->
            TaskUiFilterHelper.applyFilters(list, filters, q, st)
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )


    val uiState: StateFlow<TasksUiState> =
        combine(
            getAllTasksUseCase(),
            activeFilters,
            search,
            status
        ) { list, filters, q, st ->
            val filteredTasks = TaskUiFilterHelper.applyFilters(list, filters, q, st)
            if (list.isEmpty()) {
                TasksUiState.Empty
            } else {
                TasksUiState.Success(filteredTasks)
            }
        }
            .onStart {
                emit(TasksUiState.Loading)
            }
            .catch { exception ->
                emit(
                    TasksUiState.Error(
                        exception.message ?: "Something went wrong"
                    )
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = TasksUiState.Loading
            )


//
//    init {
//        viewModelScope.launch {
//            getAllTasksUseCase().collect { _tasks.value = it }
//        }
//    }


    fun deleteTask(task: Task) = viewModelScope.launch {
        deleteTaskUseCase(task)
    }

    fun toggleTaskCompletion(task: Task) = viewModelScope.launch {
        val updated = task.copy(isCompleted = !task.isCompleted)
        updateTaskUseCase(updated)

    }

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

//    val visibleTasks: StateFlow<List<Task>> =
//        combine(tasks, activeFilters, search, status) { list, filters, q, st ->
//            TaskUiFilterHelper.applyFilters(list, filters, q, st)
//        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

}