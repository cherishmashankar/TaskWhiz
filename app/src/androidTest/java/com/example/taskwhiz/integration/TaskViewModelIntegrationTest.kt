package com.example.taskwhiz.integration

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.example.taskwhiz.data.local.TaskDatabase
import com.example.taskwhiz.data.remote.TaskApiService
import com.example.taskwhiz.data.repository.TaskRepositoryImpl
import com.example.taskwhiz.domain.model.Task
import com.example.taskwhiz.domain.usecase.task.*
import com.example.taskwhiz.presentation.utils.TaskFilters
import com.example.taskwhiz.presentation.utils.TaskStatus
import com.example.taskwhiz.presentation.viewmodel.TaskViewModel
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class TaskViewModelIntegrationTest {

    private lateinit var db: TaskDatabase
    private lateinit var repository: TaskRepositoryImpl
    private lateinit var viewModel: TaskViewModel

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TaskDatabase::class.java
        ).allowMainThreadQueries().build()

        val dao = db.taskDao()

        // Fake API (not testing remote here)
        val fakeApi = object : TaskApiService {
            override suspend fun getStructuredTask(requestBody: Map<String, Any>) =
                throw UnsupportedOperationException("Not needed")
        }

        repository = TaskRepositoryImpl(dao, fakeApi)

        // Real use cases
        val getAllTasks = GetAllTasksUseCase(repository)
        val insertTask = InsertTaskUseCase(repository)
        val updateTask = UpdateTaskUseCase(repository)
        val deleteTask = DeleteTaskUseCase(repository)
        val getTaskById = GetTaskByIdUseCase(repository)

        viewModel = TaskViewModel(
            getAllTasks,
            insertTask,
            updateTask,
            deleteTask,
            getTaskById
        )
    }

    @After
    fun tearDown()= runTest {
        advanceUntilIdle()
        db.close()
    }

    // TESTS

    @Test
    fun insertTask_updatesTasksList() = runTest {
        val task = Task(title = "Test Insert", createdAt = System.currentTimeMillis())

        viewModel.tasks.test {
            viewModel.addNewTask(task)
            advanceUntilIdle()

            skipItems(1)
            val list = awaitItem()
            assertThat(list).hasSize(1)
            assertThat(list.first().title).isEqualTo("Test Insert")

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun updateTask_reflectsChanges() = runTest {
        val task = Task(title = "Original", createdAt = System.currentTimeMillis())

        viewModel.tasks.test {
            viewModel.addNewTask(task)
            advanceUntilIdle()
            skipItems(1) // skip initial empty
            val inserted = awaitItem().first()

            val updated = inserted.copy(title = "Updated", isCompleted = true)
            viewModel.updateExistingTask(updated)
            advanceUntilIdle()

            val result = awaitItem().first()
            assertThat(result.title).isEqualTo("Updated")
            assertThat(result.isCompleted).isTrue()

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun deleteTask_removesFromList() = runTest {
        val task = Task(title = "Delete Me", createdAt = System.currentTimeMillis())

        viewModel.tasks.test {
            viewModel.addNewTask(task)
            advanceUntilIdle()

            skipItems(1)
            val inserted = awaitItem()
            assertThat(inserted).hasSize(1)
            viewModel.deleteTask(inserted.first())
            val afterDelete = awaitItem()
            assertThat(afterDelete).isEmpty()
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun toggleTaskCompletion_flipsValue() = runTest {
        val task = Task(title = "Toggle Me", createdAt = System.currentTimeMillis(), isCompleted = false)

        viewModel.tasks.test {
            viewModel.addNewTask(task)
            advanceUntilIdle()
            skipItems(1)
            val inserted = awaitItem().first()
            assertThat(inserted.isCompleted).isFalse()

            viewModel.toggleTaskCompletion(inserted)
            val toggled = awaitItem().first()
            assertThat(toggled.isCompleted).isTrue()

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun getTaskById_returnsTaskOrNull() = runTest {
        val task = Task(title = "Find Me", createdAt = System.currentTimeMillis())

        viewModel.addNewTask(task)
        advanceUntilIdle()


       viewModel.getTaskById(1L).test {
            val item = awaitItem()
            assertThat(item?.title).isEqualTo("Find Me")
            cancelAndConsumeRemainingEvents()

        }

        viewModel.getTaskById(999L).test {
            val item = awaitItem()
            assertThat(item).isNull()
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun filterTasks_byStatusCompleted_onlyReturnsCompleted() = runTest {
        val now = System.currentTimeMillis()
        val incomplete = Task(title = "Incomplete", createdAt = now, isCompleted = false)
        val complete = Task(title = "Completed", createdAt = now, isCompleted = true)

        viewModel.visibleTasks.test {
            viewModel.addNewTask(incomplete)
            viewModel.addNewTask(complete)
            advanceUntilIdle()

            skipItems(1) // skip initial empty StateFlow Default value

            viewModel.status.value = TaskStatus.COMPLETED
            val filtered = awaitItem()
            advanceUntilIdle()

            assertThat(filtered).hasSize(1)
            assertThat(filtered.first().title).isEqualTo("Completed")
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun tasks_startEmpty() = runTest {
        viewModel.tasks.test {
            val initial = awaitItem()
            assertThat(initial).isEmpty()
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun insertTask_withNullFields_persistsCorrectly() = runTest {
        val task = Task(id = 1, title = "Null Fields", createdAt = System.currentTimeMillis())
        viewModel.addNewTask(task)
        advanceUntilIdle()

        viewModel.tasks.test {
            skipItems(1) // skip initial empty
            val inserted = awaitItem().first()
            assertThat(inserted.dueAt).isNull()
            assertThat(inserted.reminderAt).isNull()
            assertThat(inserted.rawInput).isNull()
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun insertTask_withEmptyTaskItems_storesCorrectly() = runTest {
        val task = Task(id = 2, title = "Empty Items", createdAt = System.currentTimeMillis(), taskItems = emptyList())
        viewModel.addNewTask(task)
        advanceUntilIdle()

        viewModel.tasks.test {
            skipItems(1)
            val inserted = awaitItem().first()
            assertThat(inserted.taskItems).isEmpty()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun toggleTaskCompletion_flipsBackAndForth() = runTest {
        val task = Task(id = 3, title = "Flip Me", createdAt = System.currentTimeMillis(), isCompleted = false)
        viewModel.addNewTask(task)
        advanceUntilIdle()

        // Skip initial empty emission
        viewModel.tasks.test {
            skipItems(1)
            val inserted = awaitItem().first()
            assertThat(inserted.isCompleted).isFalse()


            viewModel.toggleTaskCompletion(inserted)
            val toggled = awaitItem().first()
            assertThat(toggled.isCompleted).isTrue()


            viewModel.toggleTaskCompletion(toggled)
            val toggledBack = awaitItem().first()
            assertThat(toggledBack.isCompleted).isFalse()
            cancelAndConsumeRemainingEvents()
        }
    }


    @Test
    fun dueDateFilters_showCorrectTasks() = runTest {
        val now = System.currentTimeMillis()
        val todayTask = Task(id = 6, title = "Today", createdAt = now, dueAt = now)
        val overdueTask = Task(id = 7, title = "Overdue", createdAt = now, dueAt = now - 24 * 60 * 60 * 1000)

        viewModel.addNewTask(todayTask)
        viewModel.addNewTask(overdueTask)
        advanceUntilIdle()

        viewModel.toggleFilter(TaskFilters.TODAY)
        viewModel.visibleTasks.test {
            skipItems(1)
            assertThat(awaitItem()).containsExactly(todayTask)
            cancelAndIgnoreRemainingEvents()
        }

        viewModel.toggleFilter(TaskFilters.TODAY)
        viewModel.toggleFilter(TaskFilters.OVERDUE)
        viewModel.visibleTasks.test {
            skipItems(1)
            assertThat(awaitItem()).containsExactly(todayTask, overdueTask)
            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun highPriorityFilter_showsOnlyHighPriorityTasks() = runTest {
        val now = System.currentTimeMillis()
        val high = Task(id = 10, title = "High", createdAt = now, priorityLevel = 1)
        val low = Task(id = 11, title = "Low", createdAt = now, priorityLevel = 2)

        viewModel.addNewTask(high)
        viewModel.addNewTask(low)
        advanceUntilIdle()


        viewModel.toggleFilter(TaskFilters.HIGH_PRIORITY)

        viewModel.visibleTasks.test {
            skipItems(1)
            assertThat(awaitItem()).containsExactly(high)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun filterTasks_completedAndReminder_returnsOnlyCompletedWithReminders() = runTest {
        val now = System.currentTimeMillis()
        val completedWithReminder = Task(id = 7, title = "Done With Reminder", createdAt = now, isCompleted = true, reminderAt = now + 60_000)
        val completedWithoutReminder = Task(id = 8, title = "Done No Reminder", createdAt = now, isCompleted = true, reminderAt = null)
        val pendingWithReminder = Task(id = 9, title = "Pending With Reminder", createdAt = now, isCompleted = false, reminderAt = now + 60_000)

        viewModel.addNewTask(completedWithReminder)
        viewModel.addNewTask(completedWithoutReminder)
        viewModel.addNewTask(pendingWithReminder)
        advanceUntilIdle()

        viewModel.status.value = TaskStatus.COMPLETED
        viewModel.toggleFilter(TaskFilters.REMINDER)
        advanceUntilIdle()

        viewModel.visibleTasks.test {
            skipItems(1)
            assertThat(awaitItem()).containsExactly(completedWithReminder)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun filterTasks_completedOverdueHighPriority_returnsOnlyMatchingTasks() = runTest {
        val now = System.currentTimeMillis()
        val overdueCompletedHigh = Task(
            id = 10,
            title = "Overdue Completed High",
            createdAt = now,
            dueAt = now - 86_400_000, // yesterday
            isCompleted = true,
            priorityLevel = 1
        )
        val overdueCompletedLow = Task(
            id = 11,
            title = "Overdue Completed Low",
            createdAt = now,
            dueAt = now - 86_400_000,
            isCompleted = true,
            priorityLevel = 2
        )
        val todayCompletedHigh = Task(
            id = 12,
            title = "Today Completed High",
            createdAt = now,
            dueAt = now,
            isCompleted = true,
            priorityLevel = 1
        )
        val overduePendingHigh = Task(
            id = 13,
            title = "Overdue Pending High",
            createdAt = now,
            dueAt = now - 86_400_000,
            isCompleted = false,
            priorityLevel = 1
        )

        viewModel.addNewTask(overdueCompletedHigh)
        viewModel.addNewTask(overdueCompletedLow)
        viewModel.addNewTask(todayCompletedHigh)
        viewModel.addNewTask(overduePendingHigh)
        advanceUntilIdle()

        // Apply all three filters
        viewModel.status.value = TaskStatus.COMPLETED
        viewModel.toggleFilter(TaskFilters.OVERDUE)
        viewModel.toggleFilter(TaskFilters.HIGH_PRIORITY)
        advanceUntilIdle()

        viewModel.visibleTasks.test {
            skipItems(1)
            assertThat(awaitItem()).containsExactly(overdueCompletedHigh)
            cancelAndIgnoreRemainingEvents()
        }
    }



}
