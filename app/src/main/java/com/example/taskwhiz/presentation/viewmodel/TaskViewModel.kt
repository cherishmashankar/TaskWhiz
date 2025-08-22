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
            getAllTasksUseCase().collect { taskList ->
                _tasks.value = taskList
            }
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
}
