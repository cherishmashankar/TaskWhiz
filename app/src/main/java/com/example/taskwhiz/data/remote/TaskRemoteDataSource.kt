package com.example.taskwhiz.data.remote

import com.example.taskwhiz.data.remote.dto.TaskRequestDto
import com.example.taskwhiz.data.remote.dto.TaskResponseDto

class TaskRemoteDataSource(
    private val api: TaskApiService
) {

    suspend fun generateTask(text: String): TaskResponseDto {
        return api.generateTask(
            TaskRequestDto(text)
        )
    }
}