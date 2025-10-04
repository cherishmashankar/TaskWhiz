package com.example.taskwhiz

import com.example.taskwhiz.data.local.TaskDao
import com.example.taskwhiz.data.local.TaskEntity
import com.example.taskwhiz.data.mapper.toEntity
import com.example.taskwhiz.data.remote.TaskApiService
import com.example.taskwhiz.data.remote.TaskApiWrapperResponse
import com.example.taskwhiz.data.repository.TaskRepositoryImpl
import com.example.taskwhiz.domain.model.Task
import com.example.taskwhiz.domain.repository.TaskRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class TaskRepositoryImplTest {

    private lateinit var taskDao: TaskDao
    private lateinit var repository: TaskRepository

    @Before
    fun setup() {
        taskDao = mock()
        val fakeApi = object : TaskApiService {
            override suspend fun getStructuredTask(
                requestBody: Map<String, Any>
            ): TaskApiWrapperResponse {
                throw UnsupportedOperationException("Not needed for this test")
            }
        }
        repository = TaskRepositoryImpl(taskDao, fakeApi)
    }

    @Test
    fun insertTask_callsDaoInsert() = runTest {
        val task = Task(id = 1L, title = "Test Task", createdAt = System.currentTimeMillis())
        repository.insertTask(task)
        verify(taskDao).insertTask(task.toEntity())
    }

    @Test
    fun getAllTasks_returnsTasksFromDao() = runTest {
        val task = Task(id = 1L, title = "From DAO", createdAt = System.currentTimeMillis())
        val entity = task.toEntity()
        whenever(taskDao.getAllTasks()).thenReturn(flowOf(listOf(entity)))
        val result = repository.getAllTasks().first()
        assertThat(result).hasSize(1)
        assertThat(result[0].title).isEqualTo("From DAO")
    }

    @Test
    fun deleteTask_callsDaoDelete() = runTest {
        val task = Task(id = 1L, title = "Delete Me", createdAt = System.currentTimeMillis())
        repository.deleteTask(task)
        verify(taskDao).deleteTask(task.toEntity())
    }

    @Test
    fun getTaskById_returnsNullIfNotFound() = runTest {
        whenever(taskDao.getTaskById(999L)).thenReturn(flowOf(null))
        val result = repository.getTaskById(999L).first()
        assertThat(result).isNull()
    }


    @Test
    fun getAllTasks_returnsEmptyListWhenDaoEmpty() = runTest {
        whenever(taskDao.getAllTasks()).thenReturn(flowOf(emptyList()))

        val result = repository.getAllTasks().first()

        assertThat(result).isEmpty()
    }


    @Test
    fun updateTask_callsDaoUpdate() = runTest {
        val task = Task(id = 2L, title = "Update Me", createdAt = System.currentTimeMillis())
        repository.updateTask(task)
        verify(taskDao).updateTask(task.toEntity())
    }

    @Test
    fun getAllTasks_emitsMultipleValuesOverTime() = runTest {
        val entity1 = Task(id = 1L, title = "Task A", createdAt = System.currentTimeMillis()).toEntity()
        val entity2 = Task(id = 2L, title = "Task B", createdAt = System.currentTimeMillis()).toEntity()

        val flow = MutableSharedFlow<List<TaskEntity>>()
        whenever(taskDao.getAllTasks()).thenReturn(flow)

        val results = mutableListOf<List<Task>>()
        val job = launch { repository.getAllTasks().toList(results) }

        flow.emit(listOf(entity1))
        flow.emit(listOf(entity1, entity2))

        assertThat(results[0]).hasSize(1)
        assertThat(results[1]).hasSize(2)

        job.cancel()
    }

    @Test
    fun deleteTask_nonExistentTaskStillCallsDao() = runTest {
        val task = Task(id = 999L, title = "Ghost Task", createdAt = System.currentTimeMillis())
        repository.deleteTask(task)
        verify(taskDao).deleteTask(task.toEntity())
    }



}
