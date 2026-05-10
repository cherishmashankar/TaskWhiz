package com.example.taskwhiz.data.local

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks ORDER BY created_at DESC")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity): Long

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getTaskById(id: Long): Flow<TaskEntity?>


    @Query("SELECT * FROM tasks WHERE reminder_at > :currentTime")
    suspend fun getAllTasksWithFutureReminders(currentTime: Long): List<TaskEntity>


}
