package com.taskflow.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.taskflow.app.ui.screens.AddEditTaskScreen
import com.taskflow.app.ui.screens.SearchScreen
import com.taskflow.app.ui.screens.TaskListScreen
import com.taskflow.app.ui.theme.NeoBackground
import com.taskflow.app.ui.theme.TaskFlowTheme
import com.taskflow.app.ui.viewmodel.TaskViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskFlowTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(NeoBackground)
                        .statusBarsPadding()
                        .navigationBarsPadding(),
                    color = NeoBackground
                ) {
                    val navController = rememberNavController()
                    val viewModel: TaskViewModel = viewModel(factory = TaskViewModel.Factory((application as TaskFlowApplication).repository))
                    val state by viewModel.uiState.collectAsState()
                    
                    NavHost(navController = navController, startDestination = "tasks") {
                        composable("tasks") {
                            TaskListScreen(
                                viewModel = viewModel,
                                onAddTask = { navController.navigate("add") },
                                onEditTask = { task -> navController.navigate("edit/${task.id}") },
                                onSearch = { navController.navigate("search") }
                            )
                        }
                        composable("add") {
                            AddEditTaskScreen(
                                onSave = { task ->
                                    viewModel.addTask(task)
                                    navController.popBackStack()
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable(
                            "edit/{taskId}",
                            arguments = listOf(navArgument("taskId") { type = NavType.LongType })
                        ) { backStackEntry ->
                            val taskId = backStackEntry.arguments?.getLong("taskId") ?: 0L
                            AddEditTaskScreen(
                                existingTask = state.tasks.find { it.id == taskId },
                                onSave = { task ->
                                    viewModel.updateTask(task)
                                    navController.popBackStack()
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("search") {
                            SearchScreen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() },
                                onEditTask = { task -> navController.navigate("edit/${task.id}") }
                            )
                        }
                    }
                }
            }
        }
    }
}
