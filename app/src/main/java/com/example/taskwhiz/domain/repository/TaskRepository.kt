package com.example.taskwhiz.domain.repository

import com.example.taskwhiz.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    fun getAllTasks(): Flow<List<Task>>

    fun getTaskById(id: Long): Flow<Task?>

    suspend fun updateTask(task: Task)

    suspend fun insertTask(task: Task): Long

    suspend fun deleteTask(task: Task)

    suspend fun getAllTasksWithFutureReminders(currentTime: Long): List<Task>


    suspend fun generateTaskFromAI(text: String): Task
}