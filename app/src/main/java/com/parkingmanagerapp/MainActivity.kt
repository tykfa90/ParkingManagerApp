package com.parkingmanagerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.parkingmanagerapp.ui.theme.ParkingManagerAppTheme
import com.parkingmanagerapp.utility.AppSurface
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
    val snackbarHostState = remember { SnackbarHostState() }

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

    AppSurface(navController = navController, snackbarHostState = snackbarHostState)
}