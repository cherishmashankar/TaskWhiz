package com.example.taskwhiz



import app.cash.turbine.test
import com.example.taskwhiz.domain.model.Task
import com.example.taskwhiz.domain.usecase.task.DeleteTaskUseCase
import com.example.taskwhiz.domain.usecase.task.GetAllTasksUseCase
import com.example.taskwhiz.domain.usecase.task.GetTaskByIdUseCase
import com.example.taskwhiz.domain.usecase.task.InsertTaskUseCase
import com.example.taskwhiz.domain.usecase.task.UpdateTaskUseCase
import com.example.taskwhiz.presentation.utils.TaskFilters
import com.example.taskwhiz.presentation.utils.TaskStatus
import com.example.taskwhiz.presentation.viewmodel.TaskViewModel
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class TaskViewModelTest {

    private lateinit var viewModel: TaskViewModel
    private val getAllTasks: GetAllTasksUseCase = mock()
    private val insertTask: InsertTaskUseCase = mock()
    private val updateTask: UpdateTaskUseCase = mock()
    private val deleteTask: DeleteTaskUseCase = mock()
    private val getTaskById: GetTaskByIdUseCase = mock()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        runTest {
            whenever(getAllTasks()).thenReturn(flowOf(emptyList()))
        }
        viewModel = TaskViewModel(
            getAllTasks,
            insertTask,
            updateTask,
            deleteTask,
            getTaskById
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init collects tasks from use case`() = runTest {
        val task = Task(id = 1L, title = "Test Task", createdAt = System.currentTimeMillis())
        whenever(getAllTasks()).thenReturn(flowOf(listOf(task)))

        // re-init with updated flow
        viewModel = TaskViewModel(getAllTasks, insertTask, updateTask, deleteTask, getTaskById)

        advanceUntilIdle()

        viewModel.tasks.test {
            val items = awaitItem()
            assert(items.size == 1)
            assert(items[0].title == "Test Task")
        }
    }

    @Test
    fun `addNewTask calls insertTaskUseCase`() = runTest {
        val task = Task(id = 1L, title = "New Task", createdAt = System.currentTimeMillis())
        viewModel.addNewTask(task)
        advanceUntilIdle()
        verify(insertTask).invoke(task)
    }

    @Test
    fun `updateExistingTask calls updateTaskUseCase`() = runTest {
        val task = Task(id = 2L, title = "Update Me", createdAt = System.currentTimeMillis())
        viewModel.updateExistingTask(task)
        advanceUntilIdle()
        verify(updateTask).invoke(task)
    }

    @Test
    fun `deleteTask calls deleteTaskUseCase`() = runTest {
        val task = Task(id = 3L, title = "Delete Me", createdAt = System.currentTimeMillis())
        viewModel.deleteTask(task)
        advanceUntilIdle()
        verify(deleteTask).invoke(task)
    }

    @Test
    fun `toggleTaskCompletion inverts completion and calls updateTaskUseCase`() = runTest {
        val task = Task(id = 4L, title = "Toggle Me", createdAt = System.currentTimeMillis(), isCompleted = false)
        viewModel.toggleTaskCompletion(task)
        advanceUntilIdle()
        verify(updateTask).invoke(task.copy(isCompleted = true))
    }

    @Test
    fun `toggleFilter adds and removes filters`() = runTest {
        val filter = "Today"
        viewModel.activeFilters.test {
            assert(awaitItem().isEmpty()) // initially empty

            viewModel.toggleFilter(filter)
            assert(awaitItem().contains(filter))

            viewModel.toggleFilter(filter)
            assert(!awaitItem().contains(filter))
        }
    }


/*    @Test
    fun `search and status update visibleTasks`() = runTest {
        val task = Task(id = 5L, title = "Search Me", createdAt = System.currentTimeMillis())
        whenever(getAllTasks()).thenReturn(flowOf(listOf(task)))

        viewModel = TaskViewModel(getAllTasks, insertTask, updateTask, deleteTask, getTaskById)
        advanceUntilIdle()

        viewModel.search.value = "Search"
        viewModel.status.value = TaskStatus.ALL

        viewModel.visibleTasks.test {
            skipItems(1)
            val list = awaitItem()
            assert(list.any { it.title.contains("Search Me") })
        }
    }*/


    @Test
    fun `search should handle empty task titles without crashing`() = runTest {
        val task = Task(id = 1L, title = "", createdAt = System.currentTimeMillis())
        whenever(getAllTasks()).thenReturn(flowOf(listOf(task)))

        viewModel = TaskViewModel(getAllTasks, insertTask, updateTask, deleteTask, getTaskById)
        advanceUntilIdle()

        viewModel.search.value = "anything"

        viewModel.visibleTasks.test {

            val result = awaitItem()
            assertThat(result).isEmpty()
        }
    }

    @Test
    fun `status COMPLETED should return empty list when no tasks are completed`() = runTest {
        val task = Task(id = 2L, title = "Pending Task", createdAt = System.currentTimeMillis(), isCompleted = false)
        whenever(getAllTasks()).thenReturn(flowOf(listOf(task)))

        viewModel = TaskViewModel(getAllTasks, insertTask, updateTask, deleteTask, getTaskById)
        advanceUntilIdle()

        viewModel.status.value = TaskStatus.COMPLETED

        viewModel.visibleTasks.test {
            val result = awaitItem()
            assertThat(result).isEmpty()
        }
    }

    @Test
    fun `filter TODAY and OVERDUE should only match tasks that satisfy both`() = runTest {
        val now = System.currentTimeMillis()
        val todayTask = Task(id = 3L, title = "Today Task", createdAt = now, dueAt = now)
        val overdueTask = Task(id = 4L, title = "Overdue Task", createdAt = now, dueAt = now - 100000L)

        whenever(getAllTasks()).thenReturn(flowOf(listOf(todayTask, overdueTask)))
        viewModel = TaskViewModel(getAllTasks, insertTask, updateTask, deleteTask, getTaskById)
        advanceUntilIdle()

        viewModel.toggleFilter(TaskFilters.TODAY)
        viewModel.toggleFilter(TaskFilters.OVERDUE)

        viewModel.visibleTasks.test {
            val result = awaitItem()
            // none should match because a task can't be both today AND overdue
            assertThat(result).isEmpty()
        }
    }

    @Test
    fun `toggleTaskCompletion twice should revert to original state`() = runTest {
        val task = Task(id = 5L, title = "Revert Me", createdAt = System.currentTimeMillis(), isCompleted = false)

        viewModel.toggleTaskCompletion(task)
        advanceUntilIdle()
        verify(updateTask).invoke(task.copy(isCompleted = true))

        viewModel.toggleTaskCompletion(task.copy(isCompleted = true))
        advanceUntilIdle()
        verify(updateTask).invoke(task.copy(isCompleted = false))
    }

   @Test
    fun `empty filters and empty search should return all tasks`() = runTest {
        val tasks = listOf(
            Task(id = 6L, title = "Task A", createdAt = System.currentTimeMillis()),
            Task(id = 7L, title = "Task B", createdAt = System.currentTimeMillis())
        )

        whenever(getAllTasks()).thenReturn(flowOf(tasks))
        viewModel = TaskViewModel(getAllTasks, insertTask, updateTask, deleteTask, getTaskById)
        advanceUntilIdle()

        viewModel.visibleTasks.test {
            skipItems(1)
            val result = awaitItem()
            assertThat(result).containsExactlyElementsIn(tasks)
        }
    }
}
