package com.example.taskwhiz.data.helper

import com.example.taskwhiz.data.remote.TaskApiService
import com.example.taskwhiz.data.repository.AiUtils
import com.example.taskwhiz.domain.model.Task
import javax.inject.Inject

class AiSyncHelper @Inject constructor(
    private val api: TaskApiService
) {

    suspend fun getStructuredTaskFromRaw(task: Task, currentTime: Long): Task {
        val requestBody = AiUtils.createAiRequest(task.rawInput.orEmpty())
        val response = api.getStructuredTask(requestBody)
        val content = response.choices.firstOrNull()?.message?.content ?: return task

        val parsed = AiUtils.parseAiContent(content) ?: return task

        return task.copy(
            title = parsed.title,
            tag = parsed.tag,
            dueAt = parsed.dueAt,
            reminderAt = parsed.reminderAt,
            taskItems = parsed.taskItems,
            priorityLevel = parsed.priorityLevel ?: 2,
            isMessy = false,
            lastModifiedAt = currentTime
        )
    }
}