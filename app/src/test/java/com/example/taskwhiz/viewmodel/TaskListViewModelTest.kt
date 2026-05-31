package com.example.taskwhiz.viewmodel

import com.example.taskwhiz.domain.model.Task
import com.example.taskwhiz.domain.usecase.reminder.SyncTaskReminderUseCase
import com.example.taskwhiz.domain.usecase.task.DeleteTaskUseCase
import com.example.taskwhiz.domain.usecase.task.GetAllTasksUseCase
import com.example.taskwhiz.domain.usecase.task.GetTaskByIdUseCase
import com.example.taskwhiz.domain.usecase.task.UpdateTaskUseCase
import com.example.taskwhiz.presentation.ui.taskListScreen.TaskListViewModel
import com.example.taskwhiz.presentation.ui.taskListScreen.TasksUiState
import com.example.taskwhiz.presentation.utils.TaskFilters
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {

    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class TaskListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var getAllTasksUseCase: GetAllTasksUseCase
    private lateinit var updateTaskUseCase: UpdateTaskUseCase
    private lateinit var deleteTaskUseCase: DeleteTaskUseCase
    private lateinit var getTaskByIdUseCase: GetTaskByIdUseCase
    private lateinit var syncReminder: SyncTaskReminderUseCase

    @Before
    fun setup() {
        getAllTasksUseCase = mock()
        updateTaskUseCase = mock()
        deleteTaskUseCase = mock()
        getTaskByIdUseCase = mock()
        syncReminder = mock()
    }

    private fun createViewModel(): TaskListViewModel {
        return TaskListViewModel(
            getAllTasksUseCase = getAllTasksUseCase,
            updateTaskUseCase = updateTaskUseCase,
            deleteTaskUseCase = deleteTaskUseCase,
            getTaskByIdUseCase = getTaskByIdUseCase,
            syncReminder = syncReminder
        )
    }

    private fun sampleTask(
        id: Long = 1L,
        title: String = "Test Task",
        tag: String? = null,
        createdAt: Long = 1000L,
        dueAt: Long? = null,
        reminderAt: Long? = 2000L,
        isCompleted: Boolean = false
    ) = Task(
        id = id,
        title = title,
        tag = tag,
        createdAt = createdAt,
        dueAt = dueAt,
        reminderAt = reminderAt,
        isCompleted = isCompleted
    )

    @Test
    fun uiState_returnsEmptyWhenTaskListIsEmpty() = runTest {
        whenever(getAllTasksUseCase()).thenReturn(flowOf(emptyList()))

        val viewModel = createViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }

        advanceUntilIdle()

        assertThat(viewModel.uiState.value).isEqualTo(TasksUiState.Empty)
    }

    @Test
    fun uiState_returnsSuccessWhenTasksExist() = runTest {
        val task = sampleTask(title = "Buy milk")
        whenever(getAllTasksUseCase()).thenReturn(flowOf(listOf(task)))

        val viewModel = createViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }

        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertThat(state).isInstanceOf(TasksUiState.Success::class.java)

        val tasks = (state as TasksUiState.Success).tasks
        assertThat(tasks).hasSize(1)
        assertThat(tasks[0].title).isEqualTo("Buy milk")
    }

    @Test
    fun uiState_returnsErrorWhenUseCaseThrowsException() = runTest {
        whenever(getAllTasksUseCase()).thenReturn(
            flow {
                throw RuntimeException("Database error")
            }
        )

        val viewModel = createViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }

        advanceUntilIdle()

        assertThat(viewModel.uiState.value)
            .isEqualTo(TasksUiState.Error("Database error"))
    }

    @Test
    fun deleteTask_clearsReminderAndDeletesTask() = runTest {
        whenever(getAllTasksUseCase()).thenReturn(flowOf(emptyList()))

        val task = sampleTask(id = 2L, reminderAt = 5000L)
        val viewModel = createViewModel()

        viewModel.deleteTask(task)
        advanceUntilIdle()

        verify(syncReminder).invoke(task.copy(reminderAt = null))
        verify(deleteTaskUseCase).invoke(task)
    }

    @Test
    fun deleteTask_worksWhenReminderIsAlreadyNull() = runTest {
        whenever(getAllTasksUseCase()).thenReturn(flowOf(emptyList()))

        val task = sampleTask(id = 3L, reminderAt = null)
        val viewModel = createViewModel()

        viewModel.deleteTask(task)
        advanceUntilIdle()

        verify(syncReminder).invoke(task.copy(reminderAt = null))
        verify(deleteTaskUseCase).invoke(task)
    }

    @Test
    fun toggleTaskCompletion_marksIncompleteTaskAsCompleted() = runTest {
        whenever(getAllTasksUseCase()).thenReturn(flowOf(emptyList()))

        val task = sampleTask(id = 4L, isCompleted = false)
        val viewModel = createViewModel()

        viewModel.toggleTaskCompletion(task)
        advanceUntilIdle()

        verify(updateTaskUseCase).invoke(task.copy(isCompleted = true))
    }

    @Test
    fun toggleTaskCompletion_marksCompletedTaskAsIncomplete() = runTest {
        whenever(getAllTasksUseCase()).thenReturn(flowOf(emptyList()))

        val task = sampleTask(id = 5L, isCompleted = true)
        val viewModel = createViewModel()

        viewModel.toggleTaskCompletion(task)
        advanceUntilIdle()

        verify(updateTaskUseCase).invoke(task.copy(isCompleted = false))
    }

    @Test
    fun getTaskById_callsUseCaseWithCorrectId() {
        whenever(getAllTasksUseCase()).thenReturn(flowOf(emptyList()))

        val task = sampleTask(id = 10L)
        whenever(getTaskByIdUseCase(10L)).thenReturn(flowOf(task))

        val viewModel = createViewModel()

        viewModel.getTaskById(10L)

        verify(getTaskByIdUseCase).invoke(10L)
    }

    @Test
    fun toggleFilter_addsFilterWhenNotSelected() {
        whenever(getAllTasksUseCase()).thenReturn(flowOf(emptyList()))

        val viewModel = createViewModel()

        viewModel.toggleFilter("Today")

        assertThat(viewModel.activeFilters.value).contains("Today")
    }

    @Test
    fun toggleFilter_removesFilterWhenAlreadySelected() {
        whenever(getAllTasksUseCase()).thenReturn(flowOf(emptyList()))

        val viewModel = createViewModel()

        viewModel.toggleFilter("Today")
        viewModel.toggleFilter("Today")

        assertThat(viewModel.activeFilters.value).doesNotContain("Today")
    }

    @Test
    fun toggleFilter_allowsOnlyOneDateFilterAtATime() {
        whenever(getAllTasksUseCase()).thenReturn(flowOf(emptyList()))

        val viewModel = createViewModel()

        val firstDateFilter = TaskFilters.DATE_FILTERS.first()
        val secondDateFilter = TaskFilters.DATE_FILTERS.last()

        viewModel.toggleFilter(firstDateFilter)
        viewModel.toggleFilter(secondDateFilter)

        assertThat(viewModel.activeFilters.value).doesNotContain(firstDateFilter)
        assertThat(viewModel.activeFilters.value).contains(secondDateFilter)
    }
}