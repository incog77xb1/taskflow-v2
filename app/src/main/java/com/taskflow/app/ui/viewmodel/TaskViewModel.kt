package com.taskflow.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.taskflow.app.data.repository.TaskRepository
import com.taskflow.app.domain.model.Task
import com.taskflow.app.domain.model.TaskFilter
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class TaskUiState(
    val tasks: List<Task> = emptyList(),
    val query: String = "",
    val activeCount: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    private val _filter = MutableStateFlow(TaskFilter())

    val tasks: StateFlow<List<Task>> = _filter
        .debounce(150)
        .flatMapLatest { filter ->
            val flow = if (filter.query.isNotBlank()) {
                repository.searchTasks(filter.query)
            } else if (filter.selectedCategory != null) {
                repository.getTasksByCategory(filter.selectedCategory)
            } else {
                repository.getAllTasks()
            }
            flow
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)

    val activeTaskCount: StateFlow<Int> = repository.getActiveTaskCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val uiState: StateFlow<TaskUiState> = combine(tasks, _filter, activeTaskCount, _isLoading, _error) { tasks, filter, activeCount, isLoading, error ->
        TaskUiState(
            tasks = tasks,
            query = filter.query,
            activeCount = activeCount,
            isLoading = isLoading,
            error = error
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TaskUiState())

    fun updateFilter(newFilter: TaskFilter) {
        _filter.value = newFilter
    }

    fun updateQuery(query: String) {
        _filter.value = _filter.value.copy(query = query)
    }

    fun selectCategory(category: String?) {
        _filter.value = _filter.value.copy(selectedCategory = category)
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            try {
                repository.addTask(task)
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to add task: ${e.message}"
            }
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            try {
                repository.updateTask(task)
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to update task: ${e.message}"
            }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            try {
                repository.deleteTask(task)
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to delete task: ${e.message}"
            }
        }
    }

    fun toggleCompleted(task: Task) {
        viewModelScope.launch {
            try {
                repository.setTaskCompleted(task.id, !task.isCompleted)
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to update task: ${e.message}"
            }
        }
    }

    fun deleteCompletedTasks() {
        viewModelScope.launch {
            try {
                repository.deleteCompletedTasks()
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to delete completed tasks: ${e.message}"
            }
        }
    }

    fun clearError() {
        _error.value = null
    }

    class Factory(private val repository: TaskRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
                return TaskViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
