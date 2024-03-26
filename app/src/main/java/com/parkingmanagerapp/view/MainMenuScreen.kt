package com.parkingmanagerapp.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.parkingmanagerapp.utility.Screen

@Composable
fun MainMenuScreen(navController: NavController) {
    Column {
        Button(onClick = { navController.navigate(Screen.ParkingSlots.route) }) {
            Text("View Parking Slots")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Add a button for the Test Menu
        Button(onClick = { navController.navigate(Screen.TestMenu.route) }) {
            Text("Test Menu")
        }

        // TODO: Add remaining buttons to the Main Menu.
    }
}