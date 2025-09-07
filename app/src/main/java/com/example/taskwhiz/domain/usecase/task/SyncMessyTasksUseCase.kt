package com.example.taskwhiz.domain.usecase.task

import com.example.taskwhiz.data.helper.AiSyncHelper
import com.example.taskwhiz.domain.repository.TaskRepository
import javax.inject.Inject

class SyncMessyTasksUseCase @Inject constructor(
    private val repository: TaskRepository,
    private val aiSyncHelper: AiSyncHelper
) {
    suspend operator fun invoke(currentTime: Long) {
        val messyTasks = repository.getMessyTasks()

        for (task in messyTasks) {
            try {
                val updatedTask = aiSyncHelper.getStructuredTaskFromRaw(task, currentTime)
                repository.updateTask(updatedTask)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}