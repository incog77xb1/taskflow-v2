package com.taskflow.app.domain.model

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
        get() = !isCompleted && dueDate != null && dueDate!! < System.currentTimeMillis() && dueDate!! < System.currentTimeMillis() - 86400000L // end of day

    companion object {
        const val PRIORITY_LOW = 0
        const val PRIORITY_MEDIUM = 1
        const val PRIORITY_HIGH = 2
        const val PRIORITY_URGENT = 3
    }
}

enum class Priority(val value: Int, val label: String, val color: androidx.compose.ui.graphics.Color) {
    LOW(Task.PRIORITY_LOW, "Low", androidx.compose.ui.graphics.Color(0xFF4CAF50)),
    MEDIUM(Task.PRIORITY_MEDIUM, "Medium", androidx.compose.ui.graphics.Color(0xFFFFC107)),
    HIGH(Task.PRIORITY_HIGH, "High", androidx.compose.ui.graphics.Color(0xFFFF9800)),
    URGENT(Task.PRIORITY_URGENT, "Urgent", androidx.compose.ui.graphics.Color(0xFFF44336));

    companion object {
        fun from(value: Int): Priority = entries.find { it.value == value } ?: MEDIUM
    }
}
