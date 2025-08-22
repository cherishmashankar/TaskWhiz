package com.example.taskwhiz.domain.repository

import com.example.taskwhiz.domain.model.AppTheme
import com.example.taskwhiz.domain.model.Language
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    val languageFlow: Flow<Language>
    val themeFlow: Flow<AppTheme>

    suspend fun setLanguage(language: Language)
    suspend fun setTheme(theme: AppTheme)
}