package com.example.taskwhiz.data.repository

import com.example.taskwhiz.data.preferences.PreferencesManager
import com.example.taskwhiz.domain.model.AppTheme
import com.example.taskwhiz.domain.model.Language
import com.example.taskwhiz.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesRepositoryImpl(
    private val manager: PreferencesManager
) : PreferencesRepository {

    override val languageFlow: Flow<Language> =
        manager.languageFlow.map { lang ->
            when (lang) {
                "German" -> Language.GERMAN
                else -> Language.ENGLISH
            }
        }

    override val themeFlow: Flow<AppTheme> =
        manager.themeFlow.map { theme ->
            when (theme) {
                "Dark" -> AppTheme.DARK
                else -> AppTheme.LIGHT
            }
        }

    override suspend fun setLanguage(language: Language) {
        val value = when (language) {
            Language.GERMAN -> "German"
            Language.ENGLISH -> "English"
        }
        manager.setLanguage(value)
    }

    override suspend fun setTheme(theme: AppTheme) {
        val value = when (theme) {
            AppTheme.DARK -> "Dark"
            AppTheme.LIGHT -> "Light"
        }
        manager.setTheme(value)
    }
}
