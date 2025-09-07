package com.example.taskwhiz.domain.usecase.settings

import com.example.taskwhiz.domain.model.AppTheme
import com.example.taskwhiz.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetThemeUseCase @Inject constructor(
    private val repository: PreferencesRepository
) {
    operator fun invoke(): Flow<AppTheme> = repository.themeFlow
}