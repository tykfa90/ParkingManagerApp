package com.parkingmanagerapp.utility

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.parkingmanagerapp.view.adminPanel.AdminPanelScreen
import com.parkingmanagerapp.view.adminPanel.parkingSlotManagement.AdminParkingSlotScreen
import com.parkingmanagerapp.view.adminPanel.userManagement.AdminUserAccountScreen
import com.parkingmanagerapp.view.regUserPanel.MainMenuScreen
import com.parkingmanagerapp.view.regUserPanel.ParkingSlotScreen
import com.parkingmanagerapp.view.regUserPanel.SplashScreen
import com.parkingmanagerapp.view.regUserPanel.userOwnAccountManagemenet.EditUserProfileScreen
import com.parkingmanagerapp.view.regUserPanel.userOwnAccountManagemenet.RegisterScreen
import com.parkingmanagerapp.view.regUserPanel.userOwnAccountManagemenet.SignInScreen
import com.parkingmanagerapp.view.regUserPanel.userOwnAccountManagemenet.UserProfileScreen

@Composable
fun AppNavHost(navController: NavHostController, snackbarHostState: SnackbarHostState) {
    NavHost(navController = navController, startDestination = Screen.SplashScreen.route) {
        composable(Screen.SplashScreen.route) {
            SplashScreen(navController, authViewModel = hiltViewModel())
        }
        composable(Screen.SignIn.route) {
            SignInScreen(navController, snackbarHostState = snackbarHostState)
        }
        composable(Screen.Register.route) {
            RegisterScreen(navController, snackbarHostState = snackbarHostState)
        }
        composable(Screen.Home.route) {
            MainMenuScreen(navController, snackbarHostState)
        }
        composable(Screen.ParkingSlots.route) {
            ParkingSlotScreen(navController, snackbarHostState = snackbarHostState)
        }
        composable(Screen.UserProfile.route) {
            UserProfileScreen(navController, snackbarHostState = snackbarHostState)
        }
        composable(Screen.EditUserProfile.route) {
            EditUserProfileScreen(
                navController,
                snackbarHostState = snackbarHostState
            )
        }
        composable(Screen.AdminMenu.route) {
            AdminPanelScreen(
                navController,
                snackbarHostState = snackbarHostState
            )
        }
        composable(Screen.AdminMenuUserAccounts.route) {
            AdminUserAccountScreen(
                navController, snackbarHostState = snackbarHostState
            )
        }
        composable(Screen.AdminMenuParkingSlots.route) {
            AdminParkingSlotScreen(navController, snackbarHostState = snackbarHostState)
        }
    }
}