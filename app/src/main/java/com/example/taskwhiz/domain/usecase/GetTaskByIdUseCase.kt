package com.example.taskwhiz.domain.usecase

import com.example.taskwhiz.domain.model.Task
import com.example.taskwhiz.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTaskByIdUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    operator fun invoke(id: Long): Flow<Task?> {
        return repository.getTaskById(id)
    }
}