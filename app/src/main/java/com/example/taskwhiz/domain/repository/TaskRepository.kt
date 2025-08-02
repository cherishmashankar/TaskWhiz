package com.example.taskwhiz.domain.repository

import com.example.taskwhiz.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    suspend fun getAllTasks(): Flow<List<Task>>
    suspend fun getMessyTasks(): List<Task>
    suspend fun updateTask(task: Task)
    suspend fun insertTask(task: Task)
    suspend fun insertMessyNote(rawInput: String): Long
    suspend fun deleteTask(task: Task)
}