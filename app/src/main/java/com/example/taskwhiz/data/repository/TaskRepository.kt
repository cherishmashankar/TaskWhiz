package com.example.taskwhiz.data.repository




import com.example.taskwhiz.data.local.TaskDao
import com.example.taskwhiz.data.local.TaskEntity
import com.example.taskwhiz.data.remote.TaskApiService
import com.example.taskwhiz.data.remote.TaskResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TaskRepository(
    private val taskDao: TaskDao,
    private val taskApiService: TaskApiService
) {

    suspend fun insertTask(task: TaskEntity) = taskDao.insertTask(task)

    suspend fun getMessyTasks(): List<TaskEntity> = taskDao.getMessyTasks()

    suspend fun syncMessyTasks(currentTime: Long) = withContext(Dispatchers.IO) {
        val messyTasks = getMessyTasks()

        messyTasks.forEach { task ->
            try {
                val requestBody = AiUtils.createAiRequest(task.rawInput.orEmpty())

                val apiResponse = taskApiService.getStructuredTask(requestBody)
                val content = apiResponse.choices.firstOrNull()?.message?.content ?: return@forEach

                val structuredTask: TaskResponse = AiUtils.parseAiContent(content) ?: return@forEach

                val updatedTask = task.copy(
                    title = structuredTask.title,
                    tag = structuredTask.tag,
                    dueAt = structuredTask.dueAt,
                    reminderAt = structuredTask.reminderAt,
                    taskItems = structuredTask.taskItems,
                    priorityLevel = structuredTask.priorityLevel ?: 2,
                    isMessy = false,
                    lastModifiedAt = currentTime
                )

                taskDao.updateTask(updatedTask)

            } catch (e: Exception) {
                e.printStackTrace()
                // Optionally log or retry failed sync
            }
        }
    }
}