package com.example.taskwhiz.data.repository


import com.example.taskwhiz.data.local.TaskDao
import com.example.taskwhiz.data.mapper.toEntity
import com.example.taskwhiz.data.mapper.toDomain
import com.example.taskwhiz.data.remote.TaskApiService
import com.example.taskwhiz.data.remote.TaskResponse
import com.example.taskwhiz.di.IoDispatcher
import com.example.taskwhiz.domain.model.Task
import com.example.taskwhiz.domain.repository.TaskRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao,
    private val taskApiService: TaskApiService,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : TaskRepository {

    override suspend fun insertTask(task: Task) {
        withContext(ioDispatcher) {
            taskDao.insertTask(task.toEntity())
        }
    }

    override suspend fun deleteTask(task: Task) {
        withContext(ioDispatcher) {
            taskDao.deleteTask(task.toEntity())
        }
    }

    override fun getAllTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks()
            .map { entities ->
                entities.map { it.toDomain() }
            }
            .flowOn(ioDispatcher)
    }

    override suspend fun updateTask(task: Task) {
        withContext(ioDispatcher) {
            taskDao.updateTask(task.toEntity())
        }
    }

    override fun getTaskById(id: Long): Flow<Task?> {
        return taskDao.getTaskById(id)
            .map { entity ->
                entity?.toDomain()
            }
            .flowOn(ioDispatcher)
    }
}