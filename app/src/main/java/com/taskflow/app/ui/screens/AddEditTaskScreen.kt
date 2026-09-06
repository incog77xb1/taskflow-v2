package com.taskflow.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.taskflow.app.domain.model.Categories
import com.taskflow.app.domain.model.Task
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskScreen(existingTask: Task? = null, onSave: (Task) -> Unit, onBack: () -> Unit) {
    var title by remember { mutableStateOf(existingTask?.title ?: "") }
    var description by remember { mutableStateOf(existingTask?.description ?: "") }
    var category by remember { mutableStateOf(existingTask?.category ?: "General") }
    var priority by remember { mutableIntStateOf(existingTask?.priority ?: Task.PRIORITY_MEDIUM) }
    var dueDate by remember { mutableStateOf(existingTask?.dueDate) }
    var titleError by remember { mutableStateOf(false) }
    val isEdit = existingTask != null
    val dateFormatter = remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) }

    Scaffold(
        topBar = { TopAppBar(title = { Text(if (isEdit) "Edit Task" else "New Task") }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") } }) }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedTextField(value = title, onValueChange = { title = it; titleError = false }, label = { Text("Title *") }, isError = titleError, supportingText = if (titleError) {{ Text("Required") }} else null, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") }, minLines = 3, modifier = Modifier.fillMaxWidth())
            CategorySelector(selected = category, onSelect = { category = it })
            PrioritySelector(selected = priority, onSelect = { priority = it })
            OutlinedTextField(value = dueDate?.let { dateFormatter.format(Date(it)) } ?: "", onValueChange = {}, readOnly = true, label = { Text("Due Date") }, leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = { if (title.isBlank()) { titleError = true; return@Button }; onSave(Task(id = existingTask?.id ?: 0, title = title.trim(), description = description.trim(), category = category, priority = priority, dueDate = dueDate, createdAt = existingTask?.createdAt ?: System.currentTimeMillis(), isCompleted = existingTask?.isCompleted ?: false)) }, modifier = Modifier.fillMaxWidth()) { Text(if (isEdit) "Update" else "Create") }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategorySelector(selected: String, onSelect: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(value = selected, onValueChange = {}, readOnly = true, label = { Text("Category") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) }, modifier = Modifier.fillMaxWidth().menuAnchor())
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            Categories.list.forEach { cat -> DropdownMenuItem(text = { Text(cat) }, onClick = { onSelect(cat); expanded = false }) }
        }
    }
}

@Composable
private fun PrioritySelector(selected: Int, onSelect: (Int) -> Unit) {
    Column {
        Text("Priority", style = MaterialTheme.typography.labelLarge)
        Spacer(modifier = Modifier.height(4.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(Task.PRIORITY_LOW to "Low", Task.PRIORITY_MEDIUM to "Med", Task.PRIORITY_HIGH to "High", Task.PRIORITY_URGENT to "Urgent").forEach { (v, l) ->
                FilterChip(selected = selected == v, onClick = { onSelect(v) }, label = { Text(l) })
            }
        }
    }
}
