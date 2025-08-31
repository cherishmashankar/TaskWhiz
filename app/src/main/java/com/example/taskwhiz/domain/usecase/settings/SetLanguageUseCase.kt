package com.example.taskwhiz.domain.usecase


import com.example.taskwhiz.domain.model.Language
import com.example.taskwhiz.domain.repository.PreferencesRepository
import javax.inject.Inject

class SetLanguageUseCase @Inject constructor(
    private val repository: PreferencesRepository
) {
    suspend operator fun invoke(language: Language) {
        repository.setLanguage(language)
    }
}