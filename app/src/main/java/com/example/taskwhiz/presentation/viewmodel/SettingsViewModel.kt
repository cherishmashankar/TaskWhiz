package com.example.taskwhiz.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskwhiz.domain.model.Language
import com.example.taskwhiz.domain.model.AppTheme
import com.example.taskwhiz.domain.usecase.GetLanguageUseCase
import com.example.taskwhiz.domain.usecase.GetThemeUseCase
import com.example.taskwhiz.domain.usecase.SetLanguageUseCase
import com.example.taskwhiz.domain.usecase.SetThemeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getLanguageUseCase: GetLanguageUseCase,
    private val getThemeUseCase: GetThemeUseCase,
    private val setLanguageUseCase: SetLanguageUseCase,
    private val setThemeUseCase: SetThemeUseCase
) : ViewModel() {

    private val _language = MutableStateFlow(Language.ENGLISH)
    val language: StateFlow<Language> = _language

    private val _theme = MutableStateFlow(AppTheme.LIGHT)
    val theme: StateFlow<AppTheme> = _theme

    init {
        getLanguageUseCase().onEach { _language.value = it }.launchIn(viewModelScope)
        getThemeUseCase().onEach { _theme.value = it }.launchIn(viewModelScope)
    }

    fun changeLanguage(language: Language) = viewModelScope.launch {
        setLanguageUseCase(language)
    }

    fun changeTheme(theme: AppTheme) = viewModelScope.launch {
        setThemeUseCase(theme)
    }
}
