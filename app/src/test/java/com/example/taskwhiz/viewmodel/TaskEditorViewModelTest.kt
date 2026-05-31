package com.example.taskwhiz.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.example.taskwhiz.domain.model.Task
import com.example.taskwhiz.domain.usecase.reminder.SyncTaskReminderUseCase
import com.example.taskwhiz.domain.usecase.task.DeleteTaskUseCase
import com.example.taskwhiz.domain.usecase.task.GetTaskByIdUseCase
import com.example.taskwhiz.domain.usecase.task.InsertTaskUseCase
import com.example.taskwhiz.domain.usecase.task.UpdateTaskUseCase
import com.example.taskwhiz.presentation.ui.taskEditorScreen.TaskEditorViewModel
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class TaskEditorViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var insertTaskUseCase: InsertTaskUseCase
    private lateinit var updateTaskUseCase: UpdateTaskUseCase
    private lateinit var deleteTaskUseCase: DeleteTaskUseCase
    private lateinit var getTaskByIdUseCase: GetTaskByIdUseCase
    private lateinit var syncReminder: SyncTaskReminderUseCase

    @Before
    fun setup() {
        insertTaskUseCase = mock()
        updateTaskUseCase = mock()
        deleteTaskUseCase = mock()
        getTaskByIdUseCase = mock()
        syncReminder = mock()
    }

    private fun createViewModel(
        taskId: Long = -1L
    ): TaskEditorViewModel {
        return TaskEditorViewModel(
            savedStateHandle = SavedStateHandle(mapOf("taskId" to taskId)),
            insertTaskUseCase = insertTaskUseCase,
            updateTaskUseCase = updateTaskUseCase,
            deleteTaskUseCase = deleteTaskUseCase,
            getTaskByIdUseCase = getTaskByIdUseCase,
            syncReminder = syncReminder
        )
    }

    private fun sampleTask(
        id: Long = 0L,
        title: String = "Test Task",
        reminderAt: Long? = 2000L
    ) = Task(
        id = id,
        title = title,
        createdAt = 1000L,
        reminderAt = reminderAt
    )

    @Test
    fun task_isNullWhenTaskIdIsMissing() {
        val viewModel = createViewModel(taskId = -1L)

        assertThat(viewModel.task.value).isNull()
    }

    @Test
    fun task_loadsTaskWhenTaskIdExists() = runTest {
        val task = sampleTask(id = 5L, title = "Existing Task")

        whenever(getTaskByIdUseCase(5L)).thenReturn(flowOf(task))

        val viewModel = createViewModel(taskId = 5L)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.task.collect {}
        }

        advanceUntilIdle()

        assertThat(viewModel.task.value).isEqualTo(task)
    }

    @Test
    fun saveTask_insertsTaskWhenIdIsZero() = runTest {
        val task = sampleTask(id = 0L)
        whenever(insertTaskUseCase(task)).thenReturn(10L)

        val viewModel = createViewModel()

        viewModel.saveTask(task)
        advanceUntilIdle()

        verify(insertTaskUseCase).invoke(task)
    }

    @Test
    fun saveTask_syncsReminderWithGeneratedIdWhenNewTaskInserted() = runTest {
        val task = sampleTask(id = 0L)
        whenever(insertTaskUseCase(task)).thenReturn(10L)

        val viewModel = createViewModel()

        viewModel.saveTask(task)
        advanceUntilIdle()

        verify(syncReminder).invoke(task.copy(id = 10L))
    }

    @Test
    fun saveTask_updatesTaskWhenIdIsNotZero() = runTest {
        val task = sampleTask(id = 2L)

        val viewModel = createViewModel()

        viewModel.saveTask(task)
        advanceUntilIdle()

        verify(updateTaskUseCase).invoke(task)
    }

    @Test
    fun saveTask_syncsReminderWhenExistingTaskUpdated() = runTest {
        val task = sampleTask(id = 2L)

        val viewModel = createViewModel()

        viewModel.saveTask(task)
        advanceUntilIdle()

        verify(syncReminder).invoke(task)
    }

    @Test
    fun deleteTask_removesReminderBeforeDeletingTask() = runTest {
        val task = sampleTask(id = 3L, reminderAt = 5000L)

        val viewModel = createViewModel()

        viewModel.deleteTask(task)
        advanceUntilIdle()

        verify(syncReminder).invoke(task.copy(reminderAt = null))
        verify(deleteTaskUseCase).invoke(task)
    }

    @Test
    fun deleteTask_stillDeletesWhenReminderIsAlreadyNull() = runTest {
        val task = sampleTask(id = 4L, reminderAt = null)

        val viewModel = createViewModel()

        viewModel.deleteTask(task)
        advanceUntilIdle()

        verify(syncReminder).invoke(task.copy(reminderAt = null))
        verify(deleteTaskUseCase).invoke(task)
    }
}