package com.taskflow.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
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
fun SearchScreen(viewModel: TaskViewModel, onBack: () -> Unit, onEditTask: (Task) -> Unit) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Search") }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") } }) }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            OutlinedTextField(
                value = state.query,
                onValueChange = { viewModel.updateQuery(it) },
                placeholder = { Text("Search...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = { if (state.query.isNotEmpty()) IconButton(onClick = { viewModel.updateQuery("") }) { Icon(Icons.Default.Clear, contentDescription = "Clear") } },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )
            if (state.query.isNotBlank() && state.tasks.isEmpty()) {
                EmptyState(modifier = Modifier.align(Alignment.CenterHorizontally), title = "No results", subtitle = "Try different terms")
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(state.tasks, key = { it.id }) { task ->
                        TaskItem(task = task, onToggle = { viewModel.toggleCompleted(task) }, onEdit = { onEditTask(task) }, onDelete = { viewModel.deleteTask(task) })
                    }
                }
            }
        }
    }
}
