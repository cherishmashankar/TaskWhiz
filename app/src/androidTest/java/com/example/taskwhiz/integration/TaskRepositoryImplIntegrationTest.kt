package com.example.taskwhiz.integration


import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.taskwhiz.data.local.TaskDao
import com.example.taskwhiz.data.local.TaskDatabase
import com.google.common.truth.Truth.assertThat
import com.example.taskwhiz.data.remote.TaskApiService
import com.example.taskwhiz.data.repository.TaskRepositoryImpl
import com.example.taskwhiz.domain.model.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class TaskRepositoryImplIntegrationTest {

    private lateinit var db: TaskDatabase
    private lateinit var taskDao: TaskDao
    private lateinit var repository: TaskRepositoryImpl

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TaskDatabase::class.java
        ).allowMainThreadQueries().build()

        taskDao = db.taskDao()

        val fakeApi = object : TaskApiService {
            override suspend fun getStructuredTask(requestBody: Map<String, Any>) =
                throw UnsupportedOperationException("Not needed in integration test")
        }

        repository = TaskRepositoryImpl(taskDao, fakeApi)
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertTask_persistsAllFields() = runTest {
        val now = System.currentTimeMillis()
        val task = Task(
            id = 1L,
            title = "Test Insert",
            createdAt = now,
            dueAt = now + 86_400_000,
            reminderAt = now + 3_600_000,
            isCompleted = false,
            taskItems = listOf("sub1", "sub2"),
            colorCode = "#FF0000",
            priorityLevel = 0, // high
            archived = true,
            isMessy = false,
            rawInput = "Buy milk tomorrow"
        )

        repository.insertTask(task)
        val allTasks = repository.getAllTasks().first()

        assertThat(allTasks).hasSize(1)
        val saved = allTasks.first()
        assertThat(saved.title).isEqualTo("Test Insert")
        assertThat(saved.dueAt).isEqualTo(task.dueAt)
        assertThat(saved.reminderAt).isEqualTo(task.reminderAt)
        assertThat(saved.taskItems).containsExactly("sub1", "sub2")
        assertThat(saved.priorityLevel).isEqualTo(0)
        assertThat(saved.archived).isTrue()
        assertThat(saved.rawInput).isEqualTo("Buy milk tomorrow")
    }

    @Test
    fun updateTask_changesAreReflected() = runTest {
        val now = System.currentTimeMillis()
        val task = Task(id = 1L, title = "Original", createdAt = now)
        repository.insertTask(task)

        val updated = task.copy(
            title = "Updated Title",
            isCompleted = true,
            priorityLevel = 1, // medium
            colorCode = "#00FF00"
        )
        repository.updateTask(updated)

        val allTasks = repository.getAllTasks().first()
        val result = allTasks.first()
        assertThat(result.title).isEqualTo("Updated Title")
        assertThat(result.isCompleted).isTrue()
        assertThat(result.priorityLevel).isEqualTo(1)
        assertThat(result.colorCode).isEqualTo("#00FF00")
    }

    @Test
    fun deleteTask_removesTaskFromDb() = runTest {
        val task = Task(
            id = 2L,
            title = "Delete Me",
            createdAt = System.currentTimeMillis()
        )
        repository.insertTask(task)
        assertThat(repository.getAllTasks().first()).hasSize(1)

        repository.deleteTask(task)
        assertThat(repository.getAllTasks().first()).isEmpty()
    }

    @Test
    fun multipleTasks_insertAndRetrieveInOrder() = runTest {
        val now = System.currentTimeMillis()
        val task1 = Task(id = 1L, title = "Task A", createdAt = now)
        val task2 = Task(id = 2L, title = "Task B", createdAt = now + 1000)

        repository.insertTask(task1)
        repository.insertTask(task2)

        val tasks = repository.getAllTasks().first()
        // Should return in order by createdAt DESC (latest first)
        assertThat(tasks.map { it.title }).containsExactly("Task B", "Task A").inOrder()
    }

    @Test
    fun completedTask_canBeQueried() = runTest {
        val now = System.currentTimeMillis()
        val task = Task(id = 3L, title = "Completed", createdAt = now, isCompleted = true)
        repository.insertTask(task)

        val allTasks = repository.getAllTasks().first()
        assertThat(allTasks.first().isCompleted).isTrue()
    }

    @Test
    fun taskWithReminderAndDueDate_persistsCorrectly() = runTest {
        val now = System.currentTimeMillis()
        val task = Task(
            id = 4L,
            title = "Reminder Task",
            createdAt = now,
            dueAt = now + 10_000,
            reminderAt = now + 5_000
        )
        repository.insertTask(task)

        val result = repository.getAllTasks().first().first()
        assertThat(result.dueAt).isEqualTo(now + 10_000)
        assertThat(result.reminderAt).isEqualTo(now + 5_000)
    }

    @Test
    fun taskPriority_persistsCorrectly() = runTest {
        val now = System.currentTimeMillis()
        val highPriority = Task(id = 5L, title = "High", createdAt = now, priorityLevel = 0)
        val lowPriority = Task(id = 6L, title = "Low", createdAt = now, priorityLevel = 2)

        repository.insertTask(highPriority)
        repository.insertTask(lowPriority)

        val allTasks = repository.getAllTasks().first()
        val levels = allTasks.map { it.priorityLevel }
        assertThat(levels).containsExactly(0, 2)
    }

    @Test
    fun insertTask_withNullFields_persistsAndRetrievesCorrectly() = runTest {
        val now = System.currentTimeMillis()
        val task = Task(
            id = 10L,
            title = "Null Fields",
            createdAt = now,
            dueAt = null,
            reminderAt = null,
            rawInput = null
        )

        repository.insertTask(task)
        val result = repository.getAllTasks().first().first()

        assertThat(result.dueAt).isNull()
        assertThat(result.reminderAt).isNull()
        assertThat(result.rawInput).isNull()
    }

    @Test
    fun insertTask_withEmptyTaskItems_persistsAndRetrievesCorrectly() = runTest {
        val now = System.currentTimeMillis()
        val task = Task(
            id = 11L,
            title = "Empty Items",
            createdAt = now,
            taskItems = emptyList()
        )

        repository.insertTask(task)
        val result = repository.getAllTasks().first().first()

        assertThat(result.taskItems).isEmpty()
    }

    @Test
    fun toggleIsCompleted_multipleFlips_persistsCorrectly() = runTest {
        val now = System.currentTimeMillis()
        val task = Task(
            id = 12L,
            title = "Boolean Flip",
            createdAt = now,
            isCompleted = false
        )

        // Insert with false
        repository.insertTask(task)
        var result = repository.getAllTasks().first().first()
        assertThat(result.isCompleted).isFalse()

        // Update to true
        val updatedTrue = task.copy(isCompleted = true)
        repository.updateTask(updatedTrue)
        result = repository.getAllTasks().first().first()
        assertThat(result.isCompleted).isTrue()

        // Update back to false
        val updatedFalse = updatedTrue.copy(isCompleted = false)
        repository.updateTask(updatedFalse)
        result = repository.getAllTasks().first().first()
        assertThat(result.isCompleted).isFalse()
    }


}

