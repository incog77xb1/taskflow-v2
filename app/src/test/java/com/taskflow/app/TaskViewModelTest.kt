package com.taskflow.app

import com.taskflow.app.domain.model.Priority
import com.taskflow.app.domain.model.Task
import org.junit.Assert.*
import org.junit.Test

class TaskViewModelTest {
    @Test
    fun `task creation with defaults`() {
        val task = Task(title = "Test")
        assertEquals(0, task.id)
        assertEquals("Test", task.title)
        assertFalse(task.isCompleted)
        assertEquals(Task.PRIORITY_MEDIUM, task.priority)
    }

    @Test
    fun `task priority constants`() {
        assertEquals(0, Task.PRIORITY_LOW)
        assertEquals(1, Task.PRIORITY_MEDIUM)
        assertEquals(2, Task.PRIORITY_HIGH)
        assertEquals(3, Task.PRIORITY_URGENT)
    }

    @Test
    fun `priority from value`() {
        assertEquals("LOW", Priority.from(0).label)
        assertEquals("URGENT", Priority.from(3).label)
    }
}
