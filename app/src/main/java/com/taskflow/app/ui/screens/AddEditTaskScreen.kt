package com.taskflow.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.taskflow.app.domain.model.Categories
import com.taskflow.app.domain.model.Priority
import com.taskflow.app.domain.model.Task
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskScreen(
    existingTask: Task? = null,
    onSave: (Task) -> Unit,
    onBack: () -> Unit
) {
    var title by remember { mutableStateOf(existingTask?.title ?: "") }
    var description by remember { mutableStateOf(existingTask?.description ?: "") }
    var category by remember { mutableStateOf(existingTask?.category ?: "General") }
    var priority by remember { mutableIntStateOf(existingTask?.priority ?: Task.PRIORITY_MEDIUM) }
    var dueDate by remember { mutableStateOf(existingTask?.dueDate) }
    var titleError by remember { mutableStateOf(false) }

    val isEdit = existingTask != null
    val dateFormatter = remember { SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isEdit) "Edit Task" else "Create Task",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Button(
                        onClick = {
                            if (title.isBlank()) {
                                titleError = true
                                return@Button
                            }
                            onSave(
                                Task(
                                    id = existingTask?.id ?: 0,
                                    title = title.trim(),
                                    description = description.trim(),
                                    category = category,
                                    priority = priority,
                                    dueDate = dueDate,
                                    createdAt = existingTask?.createdAt ?: System.currentTimeMillis(),
                                    isCompleted = existingTask?.isCompleted ?: false
                                )
                            )
                        },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isEdit) "Save" else "Create")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Title field
            OutlinedTextField(
                value = title,
                onValueChange = {
                    title = it
                    titleError = false
                },
                label = { Text("Task Title") },
                placeholder = { Text("What needs to be done?") },
                isError = titleError,
                supportingText = if (titleError) {
                    { Text("Title cannot be empty", color = MaterialTheme.colorScheme.error) }
                } else null,
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )

            // Description field
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Notes & Details (optional)") },
                placeholder = { Text("Add any extra context...") },
                minLines = 4,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )

            // Category Selector
            Text("Category", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Categories.list.forEach { cat ->
                    val isSelected = category == cat
                    FilterChip(
                        selected = isSelected,
                        onClick = { category = cat },
                        label = { Text(cat) },
                        shape = RoundedCornerShape(12.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }

            // Priority Selector
            Text("Priority", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    Task.PRIORITY_LOW to "Low",
                    Task.PRIORITY_MEDIUM to "Medium",
                    Task.PRIORITY_HIGH to "High",
                    Task.PRIORITY_URGENT to "Urgent"
                ).forEach { (pVal, label) ->
                    val isSelected = priority == pVal
                    val pColor = Priority.from(pVal).color
                    FilterChip(
                        selected = isSelected,
                        onClick = { priority = pVal },
                        label = { Text(label, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                        shape = RoundedCornerShape(12.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = pColor,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            // Quick Due Date Actions
            Text("Due Date", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val now = System.currentTimeMillis()
                val today = remember { now + 86400000L }
                val tomorrow = remember { now + 86400000L * 2 }
                val nextWeek = remember { now + 86400000L * 7 }

                SuggestionChip(
                    onClick = { dueDate = today },
                    label = { Text("Today") },
                    shape = RoundedCornerShape(10.dp)
                )
                SuggestionChip(
                    onClick = { dueDate = tomorrow },
                    label = { Text("Tomorrow") },
                    shape = RoundedCornerShape(10.dp)
                )
                SuggestionChip(
                    onClick = { dueDate = nextWeek },
                    label = { Text("Next Week") },
                    shape = RoundedCornerShape(10.dp)
                )
                if (dueDate != null) {
                    SuggestionChip(
                        onClick = { dueDate = null },
                        label = { Text("Clear", color = MaterialTheme.colorScheme.error) },
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            }

            if (dueDate != null) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.DateRange, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Text(
                            text = "Due: ${dateFormatter.format(Date(dueDate!!))}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}
