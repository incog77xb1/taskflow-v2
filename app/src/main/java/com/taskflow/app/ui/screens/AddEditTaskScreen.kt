package com.taskflow.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.taskflow.app.domain.model.Categories
import com.taskflow.app.domain.model.Priority
import com.taskflow.app.domain.model.Task
import com.taskflow.app.ui.components.NeoCard
import com.taskflow.app.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

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
        containerColor = NeoBackground,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(NeoSurface)
                        .border(2.dp, NeoBorder, RoundedCornerShape(10.dp))
                        .clickable { onBack() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NeoDark)
                }

                Text(
                    text = if (isEdit) "Edit Task" else "New Task",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    color = NeoDark
                )

                // Save Action Button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(NeoGreen)
                        .border(2.dp, NeoBorder, RoundedCornerShape(10.dp))
                        .clickable {
                            if (title.isBlank()) {
                                titleError = true
                                return@clickable
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
                        }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = if (isEdit) "Update" else "Create",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = NeoDark
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Task Title
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Task Title", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = NeoDark)
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        titleError = false
                    },
                    placeholder = { Text("e.g. Finish quarterly presentation", color = NeoMuted) },
                    isError = titleError,
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = NeoSurface,
                        unfocusedContainerColor = NeoSurface,
                        focusedBorderColor = NeoBorder,
                        unfocusedBorderColor = NeoBorder.copy(alpha = 0.5f),
                        focusedTextColor = NeoDark,
                        unfocusedTextColor = NeoDark
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                if (titleError) {
                    Text("Title is required", color = NeoPink, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Description
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Notes", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = NeoDark)
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = { Text("Add any notes, links, or context...", color = NeoMuted) },
                    minLines = 3,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = NeoSurface,
                        unfocusedContainerColor = NeoSurface,
                        focusedBorderColor = NeoBorder,
                        unfocusedBorderColor = NeoBorder.copy(alpha = 0.5f),
                        focusedTextColor = NeoDark,
                        unfocusedTextColor = NeoDark
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Category Chips
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Category", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = NeoDark)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Categories.list.forEach { cat ->
                        val isSelected = category == cat
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) NeoBlue.copy(alpha = 0.35f) else NeoSurface)
                                .border(1.5.dp, if (isSelected) NeoBorder else NeoBorder.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                .clickable { category = cat }
                                .padding(horizontal = 12.dp, vertical = 7.dp)
                        ) {
                            Text(cat, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium, fontSize = 13.sp, color = NeoDark)
                        }
                    }
                }
            }

            // Priority Selection
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Priority", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = NeoDark)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(
                        Task.PRIORITY_LOW to "LOW",
                        Task.PRIORITY_MEDIUM to "MED",
                        Task.PRIORITY_HIGH to "HIGH",
                        Task.PRIORITY_URGENT to "URGENT"
                    ).forEach { (pVal, label) ->
                        val isSelected = priority == pVal
                        val pColor = Priority.from(pVal).color
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) pColor else NeoSurface)
                                .border(1.5.dp, if (isSelected) NeoBorder else NeoBorder.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                .clickable { priority = pVal }
                                .padding(horizontal = 12.dp, vertical = 7.dp)
                        ) {
                            Text(
                                label,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 12.sp,
                                color = NeoDark
                            )
                        }
                    }
                }
            }

            // Due Date
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Due Date", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = NeoDark)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val now = System.currentTimeMillis()
                    val today = now + 86400000L
                    val tomorrow = now + 86400000L * 2
                    val nextWeek = now + 86400000L * 7

                    listOf("Today" to today, "Tomorrow" to tomorrow, "Next Week" to nextWeek).forEach { (text, time) ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(NeoSurface)
                                .border(1.5.dp, NeoBorder.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                .clickable { dueDate = time }
                                .padding(horizontal = 12.dp, vertical = 7.dp)
                        ) {
                            Text(text, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = NeoDark)
                        }
                    }

                    if (dueDate != null) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(NeoPink.copy(alpha = 0.2f))
                                .border(1.5.dp, NeoPink, RoundedCornerShape(8.dp))
                                .clickable { dueDate = null }
                                .padding(horizontal = 10.dp, vertical = 7.dp)
                        ) {
                            Text("Clear", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NeoDark)
                        }
                    }
                }

                if (dueDate != null) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = NeoSurface,
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, NeoBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.DateRange, contentDescription = null, tint = NeoDark, modifier = Modifier.size(18.dp))
                            Text(
                                text = "Due on ${dateFormatter.format(Date(dueDate!!))}",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = NeoDark
                            )
                        }
                    }
                }
            }
        }
    }
}
