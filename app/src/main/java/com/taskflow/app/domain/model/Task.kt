package com.taskflow.app.domain.model

import androidx.compose.ui.graphics.Color
import com.taskflow.app.ui.theme.*

data class Task(
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val category: String = "General",
    val priority: Int = Task.PRIORITY_MEDIUM,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val dueDate: Long? = null
) {
    val isOverdue: Boolean
        get() = !isCompleted && dueDate != null && dueDate < (System.currentTimeMillis() - 86400000L)

    companion object {
        const val PRIORITY_LOW = 0
        const val PRIORITY_MEDIUM = 1
        const val PRIORITY_HIGH = 2
        const val PRIORITY_URGENT = 3
    }
}

enum class Priority(val value: Int, val label: String, val color: Color) {
    LOW(Task.PRIORITY_LOW, "LOW", PriorityLow),
    MEDIUM(Task.PRIORITY_MEDIUM, "MED", PriorityMedium),
    HIGH(Task.PRIORITY_HIGH, "HIGH", PriorityHigh),
    URGENT(Task.PRIORITY_URGENT, "URGENT", PriorityUrgent);

    companion object {
        fun from(value: Int): Priority = entries.find { it.value == value } ?: MEDIUM
    }
}
