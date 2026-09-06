package com.taskflow.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
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
import com.taskflow.app.ui.theme.TaskFlowTheme
import com.taskflow.app.ui.viewmodel.TaskViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskFlowTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    val viewModel: TaskViewModel = viewModel(factory = TaskViewModel.Factory((application as TaskFlowApplication).repository))
                    
                    NavHost(navController = navController, startDestination = "tasks") {
                        composable("tasks") {
                            TaskListScreen(viewModel = viewModel, onAddTask = { navController.navigate("add") }, onEditTask = { task -> navController.navigate("edit/${task.id}") }, onSearch = { navController.navigate("search") })
                        }
                        composable("add") {
                            AddEditTaskScreen(onSave = { task -> viewModel.addTask(task); navController.popBackStack() }, onBack = { navController.popBackStack() })
                        }
                        composable("edit/{taskId}", arguments = listOf(navArgument("taskId") { type = NavType.LongType })) { backStackEntry ->
                            val taskId = backStackEntry.arguments?.getLong("taskId") ?: 0L
                            AddEditTaskScreen(existingTask = viewModel.uiState.value.tasks.find { it.id == taskId }, onSave = { task -> viewModel.updateTask(task); navController.popBackStack() }, onBack = { navController.popBackStack() })
                        }
                        composable("search") {
                            SearchScreen(viewModel = viewModel, onBack = { navController.popBackStack() }, onEditTask = { task -> navController.navigate("edit/${task.id}") })
                        }
                    }
                }
            }
        }
    }
}
