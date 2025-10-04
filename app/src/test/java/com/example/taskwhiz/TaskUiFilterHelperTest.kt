package com.example.taskwhiz


import com.example.taskwhiz.domain.model.Task
import com.example.taskwhiz.presentation.helpers.TaskUiFilterHelper
import com.example.taskwhiz.presentation.utils.TaskFilters
import com.example.taskwhiz.presentation.utils.TaskStatus
import org.junit.Test
import com.google.common.truth.Truth.assertThat

class TaskUiFilterHelperTest {

    private val now = System.currentTimeMillis()

    @Test
    fun `search should return matching task titles`() {
        val tasks = listOf(
            Task(id = 1, title = "Buy groceries", createdAt = now),
            Task(id = 2, title = "Finish Homework", createdAt = now),
            Task(id = 3, title = "Call Mom", createdAt = now)
        )

        val result = TaskUiFilterHelper.applyFilters(
            tasks = tasks,
            filters = emptySet(),
            search = "home",
            status = TaskStatus.ALL
        )

        assertThat(result).hasSize(1)
        assertThat(result[0].title).isEqualTo("Finish Homework")
    }

    @Test
    fun `search should be case-insensitive`() {
        val tasks = listOf(
            Task(id = 1, title = "Read Book", createdAt = now),
            Task(id = 2, title = "Go Jogging", createdAt = now)
        )

        val result = TaskUiFilterHelper.applyFilters(
            tasks = tasks,
            filters = emptySet(),
            search = "read",
            status = TaskStatus.ALL
        )

        assertThat(result).hasSize(1)
        assertThat(result[0].title).isEqualTo("Read Book")
    }

    @Test
    fun `search with no match should return empty list`() {
        val tasks = listOf(
            Task(id = 1, title = "Meeting with team", createdAt = now),
            Task(id = 2, title = "Write report", createdAt = now)
        )

        val result = TaskUiFilterHelper.applyFilters(
            tasks = tasks,
            filters = emptySet(),
            search = "shopping",
            status = TaskStatus.ALL
        )

        assertThat(result).isEmpty()
    }

    @Test
    fun `empty search should return all tasks`() {
        val tasks = listOf(
            Task(id = 1, title = "Task One", createdAt = now),
            Task(id = 2, title = "Task Two", createdAt = now)
        )

        val result = TaskUiFilterHelper.applyFilters(
            tasks = tasks,
            filters = emptySet(),
            search = "",
            status = TaskStatus.ALL
        )

        assertThat(result).hasSize(2)
    }

    @Test
    fun `task with empty title should not crash search`() {
        val tasks = listOf(
            Task(id = 1, title = "", createdAt = now),
            Task(id = 2, title = "Normal Task", createdAt = now)
        )

        val result = TaskUiFilterHelper.applyFilters(
            tasks = tasks,
            filters = emptySet(),
            search = "normal",
            status = TaskStatus.ALL
        )

        assertThat(result).containsExactly(tasks[1])
    }


    @Test
    fun `search with whitespace should return all tasks`() {
        val tasks = listOf(
            Task(id = 1, title = "Alpha", createdAt = now),
            Task(id = 2, title = "Beta", createdAt = now)
        )

        val result = TaskUiFilterHelper.applyFilters(
            tasks = tasks,
            filters = emptySet(),
            search = "   ", // whitespace only
            status = TaskStatus.ALL
        )

        assertThat(result).hasSize(2)
    }

    @Test
    fun `status COMPLETED should only return completed tasks`() {
        val tasks = listOf(
            Task(id = 1, title = "Done", createdAt = now, isCompleted = true),
            Task(id = 2, title = "Not Done", createdAt = now, isCompleted = false)
        )

        val result = TaskUiFilterHelper.applyFilters(
            tasks = tasks,
            filters = emptySet(),
            search = "",
            status = TaskStatus.COMPLETED
        )

        assertThat(result).containsExactly(tasks[0])
    }

    @Test
    fun `status PENDING should only return incomplete tasks`() {
        val tasks = listOf(
            Task(id = 1, title = "Task A", createdAt = now, isCompleted = false),
            Task(id = 2, title = "Task B", createdAt = now, isCompleted = true)
        )

        val result = TaskUiFilterHelper.applyFilters(
            tasks = tasks,
            filters = emptySet(),
            search = "",
            status = TaskStatus.PENDING
        )

        assertThat(result).containsExactly(tasks[0])
    }


    @Test
    fun `search longer than any title should return empty`() {
        val tasks = listOf(
            Task(id = 1, title = "Short", createdAt = now),
            Task(id = 2, title = "Medium Length", createdAt = now)
        )

        val result = TaskUiFilterHelper.applyFilters(
            tasks = tasks,
            filters = emptySet(),
            search = "SuperLongSearchStringThatDoesNotExist",
            status = TaskStatus.ALL
        )

        assertThat(result).isEmpty()
    }

}
