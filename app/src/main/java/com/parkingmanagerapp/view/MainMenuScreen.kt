package com.parkingmanagerapp.view

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.parkingmanagerapp.utility.Screen

@Composable
fun MainMenuScreen(navController: NavController) {
    Button(onClick = { navController.navigate(Screen.ParkingSlots.route) }) {
        Text("View Parking Slots")
    }
    // TODO: Add remaining buttons to the Main Menu.
}