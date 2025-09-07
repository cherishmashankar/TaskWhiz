package com.example.taskwhiz.domain.usecase.task

import com.example.taskwhiz.domain.repository.TaskRepository
import javax.inject.Inject

class InsertRawNoteUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(rawText: String, createdAt: Long): Long {
        return repository.insertMessyNote(rawText)
    }
}