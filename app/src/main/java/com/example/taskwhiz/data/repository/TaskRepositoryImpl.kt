package com.example.taskwhiz.data.repository


import com.example.taskwhiz.data.local.TaskDao
import com.example.taskwhiz.data.mapper.toEntity
import com.example.taskwhiz.data.mapper.toDomain
import com.example.taskwhiz.data.remote.TaskApiService
import com.example.taskwhiz.data.remote.TaskResponse
import com.example.taskwhiz.domain.model.Task
import com.example.taskwhiz.domain.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject


class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao,
    private val taskApiService: TaskApiService
) : TaskRepository {

    override suspend fun insertTask(task: Task) {
        taskDao.insertTask(task.toEntity())
    }

    override suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task.toEntity()) // Map to entity
    }

    override suspend fun getAllTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    override suspend fun updateTask(task: Task) {
        taskDao.updateTask(task.toEntity())
    }

    override fun getTaskById(id: Long): Flow<Task?> {
        return taskDao.getTaskById(id).map { it?.toDomain() }
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


}