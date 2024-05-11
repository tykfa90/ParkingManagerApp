package com.parkingmanagerapp.utility

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.parkingmanagerapp.view.MainMenuScreen
import com.parkingmanagerapp.view.ParkingSlotScreen
import com.parkingmanagerapp.view.SplashScreen
import com.parkingmanagerapp.view.TestMenuScreen
import com.parkingmanagerapp.view.adminPanel.AdminPanelScreen
import com.parkingmanagerapp.view.regUserPanel.EditUserProfileScreen
import com.parkingmanagerapp.view.regUserPanel.RegisterScreen
import com.parkingmanagerapp.view.regUserPanel.SignInScreen
import com.parkingmanagerapp.view.regUserPanel.UserProfileScreen

// Handling navigation inside the application
@Composable
fun AppNavHost(navController: NavHostController, snackbarHostState: SnackbarHostState) {
    NavHost(navController = navController, startDestination = Screen.SplashScreen.route) {
        composable(Screen.SplashScreen.route) {
            SplashScreen(navController, authViewModel = hiltViewModel())
        }
        composable(Screen.SignIn.route) {
            SignInScreen(navController, snackbarHostState = SnackbarHostState())
        }
        composable(Screen.Register.route) {
            RegisterScreen(navController, snackbarHostState = SnackbarHostState())
        }
        composable(Screen.Home.route) {
            MainMenuScreen(navController, snackbarHostState)
        }
        composable(Screen.ParkingSlots.route) {
            ParkingSlotScreen()
        }
        composable(Screen.TestMenu.route) {
            TestMenuScreen(navController, snackbarHostState = SnackbarHostState())
        }
        composable(Screen.UserProfile.route) {
            UserProfileScreen(navController, snackbarHostState = SnackbarHostState())
        }
        composable(Screen.EditUserProfile.route) {
            EditUserProfileScreen(
                navController,
                snackbarHostState = SnackbarHostState()
            )
        }
        composable(Screen.AdminMenu.route) {
            AdminPanelScreen(
                navController,
                snackbarHostState = SnackbarHostState()
            )
        }
        composable(Screen.AdminMenuUserAccounts.route) {
            AdminPanelScreen(
                navController,
                snackbarHostState = SnackbarHostState()
            )
        }
    }
}