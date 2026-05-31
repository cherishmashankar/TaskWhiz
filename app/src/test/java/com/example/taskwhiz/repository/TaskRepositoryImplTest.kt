package com.example.taskwhiz.repository

import com.example.taskwhiz.data.local.TaskDao
import com.example.taskwhiz.data.mapper.toEntity
import com.example.taskwhiz.data.remote.TaskApiService
import com.example.taskwhiz.data.remote.TaskApiWrapperResponse
import com.example.taskwhiz.data.repository.TaskRepositoryImpl
import com.example.taskwhiz.domain.model.Task
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class TaskRepositoryImplTest {

    private lateinit var taskDao: TaskDao
    private lateinit var repository: TaskRepositoryImpl

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        taskDao = mock()

        val fakeApi = object : TaskApiService {
            override suspend fun getStructuredTask(
                requestBody: Map<String, Any>
            ): TaskApiWrapperResponse {
                throw UnsupportedOperationException("Not needed")
            }
        }

        repository = TaskRepositoryImpl(
            taskDao = taskDao,
            taskApiService = fakeApi,
            ioDispatcher = testDispatcher
        )
    }

    private fun sampleTask(
        id: Long = 1L,
        title: String = "Test Task",
        createdAt: Long = 1000L
    ) = Task(
        id = id,
        title = title,
        createdAt = createdAt
    )

    @Test
    fun insertTask_callsDaoInsertAndReturnsId() = runTest {
        val task = sampleTask()
        whenever(taskDao.insertTask(task.toEntity())).thenReturn(10L)

        val result = repository.insertTask(task)

        Truth.assertThat(result).isEqualTo(10L)
        verify(taskDao).insertTask(task.toEntity())
    }

    @Test
    fun getAllTasks_returnsMappedTasksFromDao() = runTest {
        val task = sampleTask(title = "From DAO")
        whenever(taskDao.getAllTasks()).thenReturn(flowOf(listOf(task.toEntity())))

        val result = repository.getAllTasks().first()

        Truth.assertThat(result).hasSize(1)
        Truth.assertThat(result[0].title).isEqualTo("From DAO")
    }

    @Test
    fun getAllTasks_returnsEmptyListWhenDaoEmpty() = runTest {
        whenever(taskDao.getAllTasks()).thenReturn(flowOf(emptyList()))

        val result = repository.getAllTasks().first()

        Truth.assertThat(result).isEmpty()
    }

    @Test
    fun getTaskById_returnsTaskWhenFound() = runTest {
        val task = sampleTask(id = 5L, title = "Found Task")
        whenever(taskDao.getTaskById(5L)).thenReturn(flowOf(task.toEntity()))

        val result = repository.getTaskById(5L).first()

        Truth.assertThat(result).isNotNull()
        Truth.assertThat(result?.id).isEqualTo(5L)
        Truth.assertThat(result?.title).isEqualTo("Found Task")
    }

    @Test
    fun getTaskById_returnsNullWhenNotFound() = runTest {
        whenever(taskDao.getTaskById(999L)).thenReturn(flowOf(null))

        val result = repository.getTaskById(999L).first()

        Truth.assertThat(result).isNull()
    }

    @Test
    fun updateTask_callsDaoUpdate() = runTest {
        val task = sampleTask(id = 2L, title = "Update Me")

        repository.updateTask(task)

        verify(taskDao).updateTask(task.toEntity())
    }

    @Test
    fun deleteTask_callsDaoDelete() = runTest {
        val task = sampleTask(id = 3L, title = "Delete Me")

        repository.deleteTask(task)

        verify(taskDao).deleteTask(task.toEntity())
    }

    @Test
    fun deleteTask_nonExistentTaskStillCallsDao() = runTest {
        val task = sampleTask(id = 999L, title = "Ghost Task")

        repository.deleteTask(task)

        verify(taskDao).deleteTask(task.toEntity())
    }

    @Test
    fun getAllTasksWithFutureReminders_returnsMappedTasks() = runTest {
        val currentTime = 1000L
        val task = sampleTask(id = 4L, title = "Reminder Task")

        whenever(taskDao.getAllTasksWithFutureReminders(currentTime))
            .thenReturn(listOf(task.toEntity()))

        val result = repository.getAllTasksWithFutureReminders(currentTime)

        Truth.assertThat(result).hasSize(1)
        Truth.assertThat(result[0].title).isEqualTo("Reminder Task")
    }

    @Test
    fun getAllTasksWithFutureReminders_returnsEmptyList() = runTest {
        whenever(taskDao.getAllTasksWithFutureReminders(1000L))
            .thenReturn(emptyList())

        val result = repository.getAllTasksWithFutureReminders(1000L)

        Truth.assertThat(result).isEmpty()
    }
}