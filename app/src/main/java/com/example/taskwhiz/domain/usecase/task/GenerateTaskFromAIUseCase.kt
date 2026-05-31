package com.example.taskwhiz.domain.usecase.task

import com.example.taskwhiz.domain.model.Task
import com.example.taskwhiz.domain.repository.TaskRepository
import javax.inject.Inject

class GenerateTaskFromAIUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(text: String): Task {
        return repository.generateTaskFromAI(text)
    }
}