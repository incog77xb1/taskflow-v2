package com.taskflow.app.data.repository

import com.taskflow.app.data.local.TaskDao
import com.taskflow.app.data.local.TaskEntity
import com.taskflow.app.domain.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskRepository(private val taskDao: TaskDao) {

    fun getAllTasks(): Flow<List<Task>> =
        taskDao.getAllTasks().map { list -> list.map { it.toDomain() } }

    fun getActiveTasks(): Flow<List<Task>> =
        taskDao.getActiveTasks().map { list -> list.map { it.toDomain() } }

    fun getCompletedTasks(): Flow<List<Task>> =
        taskDao.getCompletedTasks().map { list -> list.map { it.toDomain() } }

    fun getTasksByCategory(category: String): Flow<List<Task>> =
        taskDao.getTasksByCategory(category).map { list -> list.map { it.toDomain() } }

    fun searchTasks(query: String): Flow<List<Task>> =
        taskDao.searchTasks(query).map { list -> list.map { it.toDomain() } }

    fun getActiveTaskCount(): Flow<Int> = taskDao.getActiveTaskCount()

    suspend fun getTaskById(id: Long): Task? =
        taskDao.getTaskById(id)?.toDomain()

    suspend fun addTask(task: Task): Long =
        taskDao.insert(task.toEntity())

    suspend fun updateTask(task: Task) =
        taskDao.update(task.toEntity())

    suspend fun deleteTask(task: Task) =
        taskDao.delete(task.toEntity())

    suspend fun deleteTaskById(id: Long) =
        taskDao.deleteById(id)

    suspend fun setTaskCompleted(id: Long, completed: Boolean) =
        taskDao.setCompleted(id, completed)

    suspend fun deleteCompletedTasks() =
        taskDao.deleteCompleted()

    private fun TaskEntity.toDomain() = Task(
        id = id,
        title = title,
        description = description,
        category = category,
        priority = priority,
        isCompleted = isCompleted,
        createdAt = createdAt,
        dueDate = dueDate
    )

    private fun Task.toEntity() = TaskEntity(
        id = id,
        title = title,
        description = description,
        category = category,
        priority = priority,
        isCompleted = isCompleted,
        createdAt = createdAt,
        dueDate = dueDate
    )
}
