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
    private val getLanguageUseCase: GetLanguageUseCase,
    private val getThemeUseCase: GetThemeUseCase,
    private val setLanguageUseCase: SetLanguageUseCase,
    private val setThemeUseCase: SetThemeUseCase,
    private val getTaskByIdUseCase: GetTaskByIdUseCase
) : ViewModel() {

    // -------------------- State --------------------
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    private val _language = MutableStateFlow(Language.ENGLISH)
    val language: StateFlow<Language> = _language

    private val _theme = MutableStateFlow(AppTheme.LIGHT)
    val theme: StateFlow<AppTheme> = _theme

    val search = MutableStateFlow("")
    val status = MutableStateFlow("All")

    private val _activeFilters = MutableStateFlow<Set<String>>(emptySet())
    val activeFilters: StateFlow<Set<String>> = _activeFilters

    private val DATE_FILTERS = setOf("Today", "Overdue")

    // -------------------- Init --------------------
    init {
        viewModelScope.launch {
            getAllTasksUseCase().collect { _tasks.value = it }
        }
        getLanguageUseCase().onEach { _language.value = it }.launchIn(viewModelScope)
        getThemeUseCase().onEach { _theme.value = it }.launchIn(viewModelScope)
    }

    // -------------------- CRUD --------------------
    fun addNewTask(task: Task) = viewModelScope.launch { insertTaskUseCase(task) }

    fun updateExistingTask(task: Task) = viewModelScope.launch { updateTaskUseCase(task) }

    fun deleteTask(task: Task) = viewModelScope.launch {
        deleteTaskUseCase(task)
        getAllTasksUseCase().collect { _tasks.value = it }
    }

    fun toggleTaskCompletion(task: Task) =
        viewModelScope.launch { updateTaskUseCase(task.copy(isCompleted = !task.isCompleted)) }

    // -------------------- Task by ID --------------------
   fun getTaskById(id: Long) = getTaskByIdUseCase(id) // returns Flow<Task?>

    // -------------------- Preferences --------------------
    fun changeLanguage(language: Language) = viewModelScope.launch { setLanguageUseCase(language) }
    fun changeTheme(theme: AppTheme) = viewModelScope.launch { setThemeUseCase(theme) }

    // -------------------- Filters --------------------
    fun toggleFilter(selected: String) {
        val current = _activeFilters.value
        _activeFilters.value =
            if (selected in DATE_FILTERS) {
                val currentDate = current intersect DATE_FILTERS
                if (selected in currentDate) current - selected else (current - DATE_FILTERS) + selected
            } else {
                if (selected in current) current - selected else current + selected
            }
    }

    // -------------------- Visible Tasks --------------------
    val visibleTasks: StateFlow<List<Task>> =
        combine(tasks, activeFilters, search, status) { list, filters, q, st ->
            list.filter { task ->
                val matchesSearch = task.title.contains(q, ignoreCase = true)
                val matchesStatus = when (st) {
                    "Completed" -> task.isCompleted
                    "Pending" -> !task.isCompleted
                    else -> true
                }
                val dateMatch = when {
                    "Today" in filters -> task.dueAt?.let { android.text.format.DateUtils.isToday(it) } == true
                    "Overdue" in filters -> task.dueAt?.let { it < System.currentTimeMillis() } == true
                    else -> true
                }
                val reminderMatch = if ("Reminder" in filters) task.reminderAt != null else true
                val priorityMatch = if ("High Priority" in filters) task.priorityLevel == 1 else true
                matchesSearch && matchesStatus && dateMatch && reminderMatch && priorityMatch
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
