package com.example.taskwhiz.data.local

import androidx.room.Insert
import androidx.room.OnConflictStrategy

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    // Insert a single task
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    // Update a task
    @Update
    suspend fun updateTask(task: TaskEntity)

    // Delete a task
    @Delete
    suspend fun deleteTask(task: TaskEntity)

    // Get a task by its ID
    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTaskById(id: Long): TaskEntity?

    // Get all active (non-archived) tasks
    @Query("SELECT * FROM tasks WHERE archived = 0 ORDER BY due_at ASC")
    fun getActiveTasks(): Flow<List<TaskEntity>>

    // Get archived tasks
    @Query("SELECT * FROM tasks WHERE archived = 1 ORDER BY last_modified_at DESC")
    fun getArchivedTasks(): Flow<List<TaskEntity>>

    // Get all messy notes that haven't been processed yet
    @Query("SELECT * FROM tasks WHERE is_messy = 1 AND raw_input IS NOT NULL")
    suspend fun getMessyTasks(): List<TaskEntity>

    // Mark a task as archived
    @Query("UPDATE tasks SET archived = 1, last_modified_at = :timestamp WHERE id = :taskId")
    suspend fun archiveTask(taskId: Long, timestamp: Long)
}
