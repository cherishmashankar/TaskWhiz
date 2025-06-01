package com.example.taskwhiz.data.repository


import com.example.taskwhiz.data.local.TaskDao
import com.example.taskwhiz.data.mapper.toEntity
import com.example.taskwhiz.data.mapper.toDomain
import com.example.taskwhiz.data.remote.TaskApiService
import com.example.taskwhiz.data.remote.TaskResponse
import com.example.taskwhiz.domain.model.Task
import com.example.taskwhiz.domain.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao,
    private val taskApiService: TaskApiService
) : TaskRepository {

    override suspend fun insertTask(task: Task) {
        taskDao.insertTask(task.toEntity())
    }

    override suspend fun insertMessyNote(rawInput: String): Long {
        val timestamp = System.currentTimeMillis()
        return taskDao.insertTask(
            Task(
                title = "",
                rawInput = rawInput,
                createdAt = timestamp,
                lastModifiedAt = timestamp,
                isMessy = true
            ).toEntity()
        )
    }

    override suspend fun getMessyTasks(): List<Task> {
        return taskDao.getMessyTasks().map { it.toDomain() }
    }

    override suspend fun getAllTasks(): List<Task> {
        return taskDao.getAllTasks().map { it.toDomain() }
    }

    override suspend fun updateTask(task: Task) {
        taskDao.updateTask(task.toEntity())
    }

    suspend fun syncMessyTasks(currentTime: Long) = withContext(Dispatchers.IO) {
        val messyTasks = taskDao.getMessyTasks()

        messyTasks.forEach { taskEntity ->
            try {
                val requestBody = AiUtils.createAiRequest(taskEntity.rawInput.orEmpty())
                val apiResponse = taskApiService.getStructuredTask(requestBody)
                val content = apiResponse.choices.firstOrNull()?.message?.content ?: return@forEach

                val structuredTask: TaskResponse = AiUtils.parseAiContent(content) ?: return@forEach

                val updatedTask = taskEntity.copy(
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
            }
        }
    }
}