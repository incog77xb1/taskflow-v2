package com.taskflow.app.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.taskflow.app.domain.model.Priority
import com.taskflow.app.domain.model.Task
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TaskItem(
    task: Task,
    onToggle: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val priority = Priority.from(task.priority)
    val checkScale = remember { Animatable(1f) }
    val cardAlpha by animateFloatAsState(
        targetValue = if (task.isCompleted) 0.55f else 1f,
        animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing),
        label = "alpha"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer { alpha = cardAlpha }
            .clip(RoundedCornerShape(20.dp))
            .clickable { onEdit() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        border = BorderStroke(
            1.dp,
            if (task.isCompleted) MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
            else priority.color.copy(alpha = 0.25f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Animated Checkbox Button
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .scale(checkScale.value)
                    .clip(CircleShape)
                    .background(
                        if (task.isCompleted) MaterialTheme.colorScheme.primary
                        else Color.Transparent
                    )
                    .border(
                        width = 2.dp,
                        color = if (task.isCompleted) MaterialTheme.colorScheme.primary
                        else priority.color.copy(alpha = 0.8f),
                        shape = CircleShape
                    )
                    .clickable {
                        onToggle()
                    },
                contentAlignment = Alignment.Center
            ) {
                if (task.isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Completed",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Task Content
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = if (task.isCompleted) FontWeight.Normal else FontWeight.SemiBold
                    ),
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (task.description.isNotBlank()) {
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Category pill
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                    ) {
                        Text(
                            text = task.category,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    // Priority tag
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = priority.color.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = priority.label,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = priority.color,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    // Due date if present
                    if (task.dueDate != null) {
                        val sdf = remember { SimpleDateFormat("MMM d", Locale.getDefault()) }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                tint = if (task.isOverdue) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = sdf.format(Date(task.dueDate)),
                                style = MaterialTheme.typography.labelSmall,
                                color = if (task.isOverdue) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                }
            }

            // Delete action
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
