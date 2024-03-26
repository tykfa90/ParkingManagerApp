package com.parkingmanagerapp.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.parkingmanagerapp.ui.theme.ParkingManagerAppTheme // Ensure this is your theme's correct import
import com.parkingmanagerapp.ui.theme.StandardScreenLayout
import com.parkingmanagerapp.utility.Screen

@Composable
fun MainMenuScreen(navController: NavController) {
    ParkingManagerAppTheme {
        StandardScreenLayout(title = "Main Menu") {
            Column(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = { navController.navigate(Screen.ParkingSlots.route) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("View Parking Slots")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { navController.navigate(Screen.TestMenu.route) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Test Menu")
                }
            }
        }
    }
}