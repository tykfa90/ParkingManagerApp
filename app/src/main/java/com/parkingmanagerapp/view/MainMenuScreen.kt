package com.parkingmanagerapp.view

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.parkingmanagerapp.ui.theme.StandardScreenLayout
import com.parkingmanagerapp.utility.Screen

@Composable
fun MainMenuScreen(navController: NavController) {
    StandardScreenLayout(
        buttons = listOf(
            "View Parking Slots" to { navController.navigate(Screen.ParkingSlots.route) },
            "Test Menu" to { navController.navigate(Screen.TestMenu.route) }
        )
    )
}