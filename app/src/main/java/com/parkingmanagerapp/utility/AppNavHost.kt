package com.parkingmanagerapp.utility

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.parkingmanagerapp.view.MainMenuScreen
import com.parkingmanagerapp.view.ParkingSlotScreen
import com.parkingmanagerapp.view.SignInScreen
import com.parkingmanagerapp.view.TestMenuScreen

// Responsible for handling navigation inside the application
@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { MainMenuScreen(navController) }
        composable(Screen.ParkingSlots.route) { ParkingSlotScreen() }
        composable(Screen.TestMenu.route) { TestMenuScreen(navController) }
        composable(Screen.SignIn.route) { SignInScreen(navController)}
    }
}