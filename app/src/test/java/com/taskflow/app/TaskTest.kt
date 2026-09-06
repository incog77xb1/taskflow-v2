package com.taskflow.app

import com.taskflow.app.domain.model.Task
import org.junit.Assert.*
import org.junit.Test

class TaskTest {
    @Test
    fun `task defaults are correct`() {
        val task = Task(title = "Test")
        assertEquals(0, task.id)
        assertEquals("Test", task.title)
        assertFalse(task.isCompleted)
        assertNull(task.dueDate)
    }

    @Test
    fun `isOverdue returns false when completed`() {
        val task = Task(title = "Test", isCompleted = true, dueDate = System.currentTimeMillis() - 86400000L)
        assertFalse(task.isOverdue)
    }

    @Test
    fun `isOverdue returns true when past due`() {
        val task = Task(title = "Test", isCompleted = false, dueDate = System.currentTimeMillis() - 86400000L * 2)
        assertTrue(task.isOverdue)
    }

    @Test
    fun `priority from value works`() {
        assertEquals(Task.PRIORITY_LOW, com.taskflow.app.domain.model.Priority.from(0).value)
        assertEquals(Task.PRIORITY_URGENT, com.taskflow.app.domain.model.Priority.from(3).value)
    }
}
