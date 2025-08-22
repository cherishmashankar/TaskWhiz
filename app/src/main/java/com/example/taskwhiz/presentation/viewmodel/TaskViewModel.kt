package com.example.taskwhiz.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskwhiz.domain.model.AppTheme
import com.example.taskwhiz.domain.model.Language
import com.example.taskwhiz.domain.model.Task
import com.example.taskwhiz.domain.usecase.DeleteTaskUseCase
import com.example.taskwhiz.domain.usecase.GetAllTasksUseCase
import com.example.taskwhiz.domain.usecase.GetLanguageUseCase
import com.example.taskwhiz.domain.usecase.GetThemeUseCase
import com.example.taskwhiz.domain.usecase.InsertTaskUseCase
import com.example.taskwhiz.domain.usecase.SetLanguageUseCase
import com.example.taskwhiz.domain.usecase.SetThemeUseCase
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

    // Preferences use cases
    private val getLanguageUseCase: GetLanguageUseCase,
    private val getThemeUseCase: GetThemeUseCase,
    private val setLanguageUseCase: SetLanguageUseCase,
    private val setThemeUseCase: SetThemeUseCase
) : ViewModel() {

    // ------------------ TASKS ------------------ //
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    init {
        viewModelScope.launch {
            getAllTasksUseCase().collect { list -> _tasks.value = list }
        }
        observePreferences()
    }

    fun updateExistingTask(task: Task) {
        viewModelScope.launch { updateTaskUseCase(task) }
    }

    fun addNewTask(task: Task) {
        viewModelScope.launch { insertTaskUseCase(task) }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch { deleteTaskUseCase(task) }
    }

    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            val updatedTask = task.copy(isCompleted = !task.isCompleted)
            updateTaskUseCase(updatedTask)
        }
    }

    // ------------------ PREFERENCES ------------------ //
    private val _language = MutableStateFlow(Language.ENGLISH)
    val language: StateFlow<Language> = _language.asStateFlow()

    private val _theme = MutableStateFlow(AppTheme.LIGHT)
    val theme: StateFlow<AppTheme> = _theme.asStateFlow()

    private fun observePreferences() {
        getLanguageUseCase().onEach { _language.value = it }.launchIn(viewModelScope)
        getThemeUseCase().onEach { _theme.value = it }.launchIn(viewModelScope)
    }

    fun changeLanguage(language: Language) {
        viewModelScope.launch { setLanguageUseCase(language) }
    }

    fun changeTheme(theme: AppTheme) {
        viewModelScope.launch { setThemeUseCase(theme) }
    }


    // ------------------ FILTERS ------------------ //
    val search = MutableStateFlow("")                  // search text
    val status = MutableStateFlow("All")

    private val _activeFilters = MutableStateFlow<Set<String>>(emptySet())
    val activeFilters: StateFlow<Set<String>> = _activeFilters


    // ---- Toggle filters
    private val DATE_FILTERS = setOf("Today", "Overdue")
    fun toggleFilter(selected: String) {
        val current = _activeFilters.value

        _activeFilters.value =
            if (selected in DATE_FILTERS) {
                val currentDate = current intersect DATE_FILTERS
                when {
                    // Tap the same active date filter -> clear (none selected)
                    selected in currentDate -> current - selected
                    // Switch date filter -> replace the previous one
                    else -> (current - DATE_FILTERS) + selected
                }
            } else {
                // Non-date filters can be combined (toggle on/off)
                if (selected in current) current - selected else current + selected
            }
    }

    // ---- Visible list derived from everything
    val visibleTasks: StateFlow<List<Task>> =
        combine(tasks, activeFilters, search, status) { list, filters, q, st ->
            list.filter { task ->
                val matchesSearch = task.title.contains(q, ignoreCase = true)

                val matchesStatus = when (st) {
                    "Completed" -> task.isCompleted
                    "Pending"   -> !task.isCompleted
                    else        -> true
                }

                val dateMatch = when {
                    "Today" in filters     -> task.dueAt?.let { android.text.format.DateUtils.isToday(it) } == true
                    "Overdue" in filters   -> task.dueAt?.let { it < System.currentTimeMillis() } == true
                    else -> true
                }

                val reminderMatch = if ("Reminder" in filters) task.reminderAt != null else true
                val priorityMatch = if ("High Priority" in filters) task.priorityLevel == 1 else true

                matchesSearch && matchesStatus && dateMatch && reminderMatch && priorityMatch
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    fun matchesActiveFilters(task: Task): Boolean {
        val filters = _activeFilters.value

        // No filters → always match
        if (filters.isEmpty()) return true

        // Date filters (only one at a time)
        val dateMatch = when {
            filters.contains("Today") -> task.dueAt?.let { android.text.format.DateUtils.isToday(it) } == true
            filters.contains("This Week") -> task.dueAt?.let { com.example.taskwhiz.utils.isThisWeek(it) } == true
            filters.contains("Overdue") -> task.dueAt?.let { it < System.currentTimeMillis() } == true
            else -> true
        }

        // Other filters (can combine)
        val reminderMatch = if (filters.contains("Reminder")) task.reminderAt != null else true
        val highPriorityMatch = if (filters.contains("High Priority")) task.priorityLevel == 1 else true

        return dateMatch && reminderMatch && highPriorityMatch
    }
}
