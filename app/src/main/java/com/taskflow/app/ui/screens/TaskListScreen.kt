package com.taskflow.app.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Search
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
        animationSpec = tween(durationMillis = 400),
        label = "progress"
    )

    Scaffold(
        containerColor = NeoBackground,
        floatingActionButton = {
            NeoButton(
                onClick = onAddTask,
                backgroundColor = NeoYellow,
                shadowOffset = 4.dp,
                cornerRadius = 14.dp
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = NeoDark, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "New Task",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
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
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "TaskFlow",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Black,
                            fontSize = 28.sp,
                            letterSpacing = (-0.5).sp
                        ),
                        color = NeoDark
                    )
                    Text(
                        text = "Minimal. Focused. Done.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = NeoMuted
                    )
                }

                // Search Icon in clean Neo style
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(NeoSurface)
                        .border(2.dp, NeoBorder, RoundedCornerShape(12.dp))
                        .clickable { onSearch() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search",
                        tint = NeoDark,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            // Progress Banner
            if (totalCount > 0) {
                NeoCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 6.dp),
                    backgroundColor = NeoSurface,
                    shadowOffset = 3.dp,
                    cornerRadius = 14.dp
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Progress",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = NeoDark
                            )
                            Text(
                                text = "$completedCount of $totalCount completed",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = NeoMuted
                            )
                        }

                        // Clean Chunky Progress Bar
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(NeoBackground)
                                .border(1.5.dp, NeoBorder, RoundedCornerShape(5.dp))
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

            // Categories Filter Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                NeoFilterChip(
                    text = "All",
                    isSelected = selectedCategory == null,
                    onClick = { selectedCategory = null }
                )

                Categories.list.forEach { category ->
                    val isSelected = selectedCategory == category
                    NeoFilterChip(
                        text = category,
                        isSelected = isSelected,
                        onClick = { selectedCategory = if (isSelected) null else category }
                    )
                }
            }

            // Tasks List
            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    state.isLoading -> {
                        CircularProgressIndicator(
                            color = NeoDark,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    filteredTasks.isEmpty() -> {
                        EmptyStateClean(
                            modifier = Modifier.align(Alignment.Center),
                            category = selectedCategory
                        )
                    }
                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
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
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) NeoYellow else NeoSurface)
            .border(2.dp, NeoBorder, RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 7.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 13.sp
            ),
            color = NeoDark
        )
    }
}

@Composable
private fun EmptyStateClean(modifier: Modifier = Modifier, category: String?) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(NeoGreen.copy(alpha = 0.3f))
                .border(2.dp, NeoBorder, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.DoneAll,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = NeoDark
            )
        }

        Text(
            text = if (category != null) "No tasks in $category" else "No pending tasks",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = NeoDark
        )
        Text(
            text = "Hit the button below to add your first task",
            style = MaterialTheme.typography.bodySmall,
            color = NeoMuted
        )
    }
}
