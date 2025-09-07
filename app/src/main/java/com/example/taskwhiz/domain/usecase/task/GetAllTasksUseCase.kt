package com.example.taskwhiz.domain.usecase.task

import com.example.taskwhiz.domain.repository.TaskRepository
import javax.inject.Inject


class GetAllTasksUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke() = repository.getAllTasks()
}