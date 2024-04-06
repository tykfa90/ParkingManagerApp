package com.parkingmanagerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.parkingmanagerapp.ui.theme.ParkingManagerAppTheme
import com.parkingmanagerapp.utility.AppNavHost
import com.parkingmanagerapp.utility.Screen
import com.parkingmanagerapp.viewModel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ParkingManagerAppTheme {
                MainContent()
            }
        }
    }
}

@Composable
fun MainContent() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val isUserAuthenticated by authViewModel.isUserAuthenticated.collectAsState(initial = null)

    LaunchedEffect(isUserAuthenticated) {
        when (isUserAuthenticated) {
            true -> {
                // Navigate to the Home screen if the user is authenticated
                if (navController.currentDestination?.route != Screen.Home.route) {
                    navController.navigate(Screen.Home.route) {
                        // Clear back stack
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            }

            false -> {
                // Navigate to the Sign In screen if the user is not authenticated
                if (navController.currentDestination?.route != Screen.SignIn.route) {
                    navController.navigate(Screen.SignIn.route) {
                        // Clear back stack
                        popUpTo(Screen.SignIn.route) { inclusive = true }
                    }
                }
            }

            null -> {} // Optional: Handle loading or initialization state
        }
    }

    AppSurface(navController = navController)
}

@Composable
fun AppSurface(navController: NavHostController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        AppNavHost(navController = navController)
    }
}