package com.taskflow.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
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
import com.taskflow.app.domain.model.Task
import com.taskflow.app.ui.components.NeoButton
import com.taskflow.app.ui.components.NeoCard
import com.taskflow.app.ui.components.TaskItem
import com.taskflow.app.ui.theme.*
import com.taskflow.app.ui.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    viewModel: TaskViewModel,
    onAddTask: () -> Unit,
    onEditTask: (Task) -> Unit,
    onSearch: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    val filteredTasks = remember(state.tasks, selectedCategory) {
        if (selectedCategory == null) state.tasks
        else state.tasks.filter { it.category == selectedCategory }
    }

    val completedCount = remember(filteredTasks) { filteredTasks.count { it.isCompleted } }
    val totalCount = filteredTasks.size
    val progress = if (totalCount > 0) completedCount.toFloat() / totalCount else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 500),
        label = "neo_progress"
    )

    Scaffold(
        containerColor = NeoBackground,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NeoBackground)
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "TASKFLOW*",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Black,
                                fontSize = 32.sp,
                                letterSpacing = (-1).sp
                            ),
                            color = NeoDark
                        )
                        Text(
                            text = "GET. SHIT. DONE.",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Black,
                                fontSize = 11.sp,
                                letterSpacing = 1.sp
                            ),
                            color = NeoPink
                        )
                    }

                    // Search Button Neo Style
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(NeoYellow)
                            .border(2.5.dp, NeoDark, RoundedCornerShape(12.dp))
                            .clickable { onSearch() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search",
                            tint = NeoDark,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            NeoButton(
                onClick = onAddTask,
                backgroundColor = NeoYellow,
                shadowOffset = 5.dp,
                cornerRadius = 14.dp
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = NeoDark, modifier = Modifier.size(22.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "NEW TASK",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = 15.sp,
                        letterSpacing = 0.5.sp
                    ),
                    color = NeoDark
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Stats / Progress Banner in Neo-Brutalism
            if (totalCount > 0) {
                NeoCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    backgroundColor = NeoCyan,
                    shadowOffset = 4.dp,
                    cornerRadius = 14.dp
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "STATUS: ${completedCount}/${totalCount} DONE",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 0.5.sp
                                ),
                                color = NeoDark
                            )
                            Text(
                                text = "${(progress * 100).toInt()}%",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black
                                ),
                                color = NeoDark
                            )
                        }

                        // Neo Progress Bar with thick border
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(14.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(NeoWhite)
                                .border(2.dp, NeoDark, RoundedCornerShape(6.dp))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(animatedProgress)
                                    .background(NeoGreen)
                            )
                        }
                    }
                }
            }

            // Category Filter Pills Neo Style
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                NeoFilterChip(
                    text = "ALL",
                    isSelected = selectedCategory == null,
                    activeColor = NeoGreen,
                    onClick = { selectedCategory = null }
                )

                Categories.list.forEach { category ->
                    val isSelected = selectedCategory == category
                    NeoFilterChip(
                        text = category.uppercase(),
                        isSelected = isSelected,
                        activeColor = NeoGreen,
                        onClick = { selectedCategory = if (isSelected) null else category }
                    )
                }
            }

            // Task List
            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    state.isLoading -> {
                        CircularProgressIndicator(
                            color = NeoDark,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    filteredTasks.isEmpty() -> {
                        EmptyStateNeo(
                            modifier = Modifier.align(Alignment.Center),
                            category = selectedCategory
                        )
                    }
                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            items(filteredTasks, key = { it.id }) { task ->
                                TaskItem(
                                    task = task,
                                    onToggle = { viewModel.toggleCompleted(task) },
                                    onEdit = { onEditTask(task) },
                                    onDelete = { viewModel.deleteTask(task) }
                                )
                            }
                            item {
                                Spacer(modifier = Modifier.height(80.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NeoFilterChip(
    text: String,
    isSelected: Boolean,
    activeColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) activeColor else NeoWhite)
            .border(2.dp, NeoDark, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Black,
                fontSize = 12.sp,
                letterSpacing = 0.5.sp
            ),
            color = NeoDark
        )
    }
}

@Composable
private fun EmptyStateNeo(modifier: Modifier = Modifier, category: String?) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        NeoCard(
            modifier = Modifier.size(90.dp),
            backgroundColor = NeoPink,
            shadowOffset = 4.dp,
            cornerRadius = 16.dp
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    modifier = Modifier.size(45.dp),
                    tint = NeoDark
                )
            }
        }

        Text(
            text = if (category != null) "NO $category TASKS!" else "NO TASKS FOUND!",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Black,
                fontSize = 22.sp
            ),
            color = NeoDark
        )
        Text(
            text = "Smash the + button to add one right now.",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = NeoMuted
        )
    }
}
