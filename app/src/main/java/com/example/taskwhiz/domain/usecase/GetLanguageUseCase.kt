package com.example.taskwhiz.domain.usecase

import com.example.taskwhiz.domain.model.Language
import com.example.taskwhiz.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLanguageUseCase @Inject constructor(
    private val repository: PreferencesRepository
) {
    operator fun invoke(): Flow<Language> = repository.languageFlow
}