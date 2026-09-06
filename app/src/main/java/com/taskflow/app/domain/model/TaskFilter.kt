package com.taskflow.app.domain.model

data class TaskFilter(
    val query: String = "",
    val showCompleted: Boolean = true,
    val selectedCategory: String? = null,
    val sortOrder: SortOrder = SortOrder.NEWEST
)

enum class SortOrder(val label: String) {
    NEWEST("Newest"),
    OLDEST("Oldest"),
    PRIORITY("Priority"),
    DUE_DATE("Due Date")
}

object Categories {
    val list = listOf(
        "General", "Work", "Personal", "Study", "Health", "Finance", "Family"
    )
}
