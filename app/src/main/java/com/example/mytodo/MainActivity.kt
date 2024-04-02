package com.example.mytodo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mytodo.Navigation.Routes
import com.example.mytodo.Screens.AddOrChange
import com.example.mytodo.Screens.MainScreen
import com.example.mytodo.Screens.TaskScreen
import com.example.mytodo.ViewModel.AddOrChangeViewModel
import com.example.mytodo.ViewModel.MainScreenViewModel
import com.example.mytodo.ui.theme.MyToDoTheme

class MainActivity : ComponentActivity() {

    private val mainViewModel = MainScreenViewModel()
    private val editViewModel = AddOrChangeViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyToDoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Routes.MAIN_SCREEN
                    ) {
                        composable(route = Routes.MAIN_SCREEN) {
                            MainScreen(
                                navController = navController, viewModel = mainViewModel
                            )
                        }
                        composable(route = Routes.ADD_OR_CHANGE_SCREEN) {
                            AddOrChange(
                                navController = navController,
                                viewModel = editViewModel,
                                mainScreenViewModel = mainViewModel
                            )
                        }
                        composable(route = Routes.TASK_SCREEN) {
                            TaskScreen(
                                navController = navController,
                                mainScreenViewModel = mainViewModel,
                                viewModel = editViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}
