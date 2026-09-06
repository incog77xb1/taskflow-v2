package com.taskflow.app

import com.taskflow.app.data.repository.TaskRepository
import com.taskflow.app.domain.model.Task
import com.taskflow.app.ui.viewmodel.TaskViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class TaskViewModelTest {
    private val repository: TaskRepository = mock()
    private lateinit var viewModel: TaskViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        whenever(repository.getAllTasks()).thenReturn(MutableStateFlow(emptyList()))
        whenever(repository.getActiveTaskCount()).thenReturn(MutableStateFlow(0))
        viewModel = TaskViewModel(repository)
    }

    @After
    fun tearDown() { Dispatchers.resetMain() }

    @Test
    fun `toggleCompleted inverts completion status`() = runTest {
        val task = Task(id = 1, title = "Test", isCompleted = false)
        viewModel.toggleCompleted(task)
        verify(repository).setTaskCompleted(1, true)
    }

    @Test
    fun `deleteTask calls repository delete`() = runTest {
        val task = Task(id = 1, title = "Test")
        viewModel.deleteTask(task)
        verify(repository).deleteTask(task)
    }

    @Test
    fun `updateQuery triggers search`() = runTest {
        viewModel.updateQuery("test")
        verify(repository).searchTasks("test")
    }
}
