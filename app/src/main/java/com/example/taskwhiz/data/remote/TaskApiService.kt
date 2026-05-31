package com.example.taskwhiz.data.remote



import com.example.taskwhiz.data.remote.dto.TaskRequestDto
import com.example.taskwhiz.data.remote.dto.TaskResponseDto
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface TaskApiService {

    @POST("/process")
    suspend fun generateTask(
        @Body request: TaskRequestDto
    ): TaskResponseDto
}
