package com.taskflow.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.taskflow.app.domain.model.Task
import com.taskflow.app.ui.components.TaskItem
import com.taskflow.app.ui.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(viewModel: TaskViewModel, onAddTask: () -> Unit, onEditTask: (Task) -> Unit, onSearch: () -> Unit) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("TaskFlow") }, actions = { IconButton(onClick = onSearch) { Icon(Icons.Default.Search, contentDescription = "Search") } }) },
        floatingActionButton = { FloatingActionButton(onClick = onAddTask, containerColor = MaterialTheme.colorScheme.primary) { Icon(Icons.Default.Add, contentDescription = "Add") } }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                state.isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                state.tasks.isEmpty() -> EmptyState(modifier = Modifier.align(Alignment.Center), title = "No tasks yet", subtitle = "Tap + to add your first task")
                else -> Column(modifier = Modifier.fillMaxSize()) {
                    ActiveTasksBar(count = state.activeCount)
                    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(state.tasks, key = { it.id }) { task ->
                            TaskItem(task = task, onToggle = { viewModel.toggleCompleted(task) }, onEdit = { onEditTask(task) }, onDelete = { viewModel.deleteTask(task) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ActiveTasksBar(count: Int) {
    Surface(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)) {
        Text("$count active tasks", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
    }
}

@Composable
fun EmptyState(modifier: Modifier = Modifier, title: String, subtitle: String) {
    Column(modifier = modifier.padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.outline)
        Spacer(modifier = Modifier.height(16.dp))
        Text(title, style = MaterialTheme.typography.titleMedium)
        Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
