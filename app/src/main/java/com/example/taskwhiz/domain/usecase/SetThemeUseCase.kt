package com.example.taskwhiz.domain.usecase

import com.example.taskwhiz.domain.model.AppTheme
import com.example.taskwhiz.domain.repository.PreferencesRepository
import javax.inject.Inject

class SetThemeUseCase @Inject constructor(
    private val repository: PreferencesRepository
) {
    suspend operator fun invoke(theme: AppTheme) {
        repository.setTheme(theme)
    }
}