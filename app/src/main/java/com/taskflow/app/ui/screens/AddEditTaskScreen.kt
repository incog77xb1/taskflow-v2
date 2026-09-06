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
import androidx.compose.ui.unit.sp
import com.taskflow.app.domain.model.Categories
import com.taskflow.app.domain.model.Priority
import com.taskflow.app.domain.model.Task
import com.taskflow.app.ui.components.NeoButton
import com.taskflow.app.ui.components.NeoCard
import com.taskflow.app.ui.theme.*
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
    val dateFormatter = remember { SimpleDateFormat("EEE, MMM d", Locale.getDefault()) }

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
                        .size(44.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(NeoWhite)
                        .border(2.5.dp, NeoDark, RoundedCornerShape(10.dp))
                        .clickable { onBack() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NeoDark)
                }

                Text(
                    text = if (isEdit) "EDIT TASK" else "CREATE TASK",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp,
                        letterSpacing = 0.5.sp
                    ),
                    color = NeoDark
                )

                // Save button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(NeoGreen)
                        .border(2.5.dp, NeoDark, RoundedCornerShape(10.dp))
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
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Text("SAVE", fontWeight = FontWeight.Black, color = NeoDark)
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
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Title field in Neo-Brutalist Box
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("TITLE *", fontWeight = FontWeight.Black, fontSize = 13.sp, color = NeoDark)
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        titleError = false
                    },
                    placeholder = { Text("What needs to get done?", color = NeoMuted) },
                    isError = titleError,
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = NeoWhite,
                        unfocusedContainerColor = NeoWhite,
                        focusedBorderColor = NeoDark,
                        unfocusedBorderColor = NeoDark,
                        focusedTextColor = NeoDark,
                        unfocusedTextColor = NeoDark
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, NeoDark, RoundedCornerShape(12.dp))
                )
                if (titleError) {
                    Text("Title is required!", color = NeoPink, fontWeight = FontWeight.Black, fontSize = 12.sp)
                }
            }

            // Description field
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("NOTES / DESCRIPTION", fontWeight = FontWeight.Black, fontSize = 13.sp, color = NeoDark)
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = { Text("Any instructions or notes...", color = NeoMuted) },
                    minLines = 3,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = NeoWhite,
                        unfocusedContainerColor = NeoWhite,
                        focusedBorderColor = NeoDark,
                        unfocusedBorderColor = NeoDark,
                        focusedTextColor = NeoDark,
                        unfocusedTextColor = NeoDark
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, NeoDark, RoundedCornerShape(12.dp))
                )
            }

            // Category Selector Neo Style
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("CATEGORY", fontWeight = FontWeight.Black, fontSize = 13.sp, color = NeoDark)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Categories.list.forEach { cat ->
                        val isSelected = category == cat
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) NeoCyan else NeoWhite)
                                .border(2.dp, NeoDark, RoundedCornerShape(8.dp))
                                .clickable { category = cat }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(cat.uppercase(), fontWeight = FontWeight.Black, fontSize = 12.sp, color = NeoDark)
                        }
                    }
                }
            }

            // Priority Selector Neo Style
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("PRIORITY LEVEL", fontWeight = FontWeight.Black, fontSize = 13.sp, color = NeoDark)
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
                                .background(if (isSelected) pColor else NeoWhite)
                                .border(2.dp, NeoDark, RoundedCornerShape(8.dp))
                                .clickable { priority = pVal }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(
                                label,
                                fontWeight = FontWeight.Black,
                                fontSize = 12.sp,
                                color = NeoDark
                            )
                        }
                    }
                }
            }

            // Due Date Selector Neo Style
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("DUE DATE", fontWeight = FontWeight.Black, fontSize = 13.sp, color = NeoDark)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val now = System.currentTimeMillis()
                    val today = now + 86400000L
                    val tomorrow = now + 86400000L * 2
                    val nextWeek = now + 86400000L * 7

                    listOf("TODAY" to today, "TOMORROW" to tomorrow, "NEXT WEEK" to nextWeek).forEach { (text, time) ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(NeoWhite)
                                .border(2.dp, NeoDark, RoundedCornerShape(8.dp))
                                .clickable { dueDate = time }
                                .padding(horizontal = 10.dp, vertical = 8.dp)
                        ) {
                            Text(text, fontWeight = FontWeight.Black, fontSize = 11.sp, color = NeoDark)
                        }
                    }

                    if (dueDate != null) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(NeoPink)
                                .border(2.dp, NeoDark, RoundedCornerShape(8.dp))
                                .clickable { dueDate = null }
                                .padding(horizontal = 10.dp, vertical = 8.dp)
                        ) {
                            Text("CLEAR", fontWeight = FontWeight.Black, fontSize = 11.sp, color = NeoDark)
                        }
                    }
                }

                if (dueDate != null) {
                    NeoCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = NeoYellow,
                        shadowOffset = 3.dp,
                        cornerRadius = 10.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.DateRange, contentDescription = null, tint = NeoDark)
                            Text(
                                text = "DUE: ${dateFormatter.format(Date(dueDate!!)).uppercase()}",
                                fontWeight = FontWeight.Black,
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
