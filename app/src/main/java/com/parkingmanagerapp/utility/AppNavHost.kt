package com.parkingmanagerapp.utility

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.parkingmanagerapp.view.MainMenuScreen
import com.parkingmanagerapp.view.ParkingSlotScreen
import com.parkingmanagerapp.view.RegisterScreen
import com.parkingmanagerapp.view.SignInScreen
import com.parkingmanagerapp.view.SplashScreen
import com.parkingmanagerapp.view.TestMenuScreen
import com.parkingmanagerapp.viewModel.AuthViewModel

// Handling navigation inside the application
@Composable
fun AppNavHost(navController: NavHostController, snackbarHostState: SnackbarHostState) {
    NavHost(navController = navController, startDestination = Screen.SplashScreen.route) {
        composable(Screen.Home.route) { MainMenuScreen(navController) }
        composable(Screen.ParkingSlots.route) { ParkingSlotScreen() }
        composable(Screen.TestMenu.route) { TestMenuScreen(navController) }
        composable(Screen.SignIn.route) { SignInScreen(navController) }
        composable(Screen.Register.route) { RegisterScreen(navController, snackbarHostState = snackbarHostState) }
        composable(Screen.SplashScreen.route) {
            SplashScreen(
                navController,
                authViewModel = AuthViewModel()
            )
        }
    }
}