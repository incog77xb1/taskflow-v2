package com.taskflow.app

import android.app.Application
import com.taskflow.app.data.local.TaskDatabase
import com.taskflow.app.data.repository.TaskRepository

class TaskFlowApplication : Application() {
    val database by lazy { TaskDatabase.getInstance(this) }
    val repository by lazy { TaskRepository(database.taskDao()) }
}
