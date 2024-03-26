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
import com.parkingmanagerapp.ui.theme.ParkingManagerAppTheme
import com.parkingmanagerapp.ui.theme.StandardScreenLayout
import com.parkingmanagerapp.utility.Screen

@Composable
fun TestMenuScreen(navController: NavController) {
    ParkingManagerAppTheme {
        StandardScreenLayout(title = "Test Menu") {
            Column(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = { navController.navigate(Screen.SignIn.route) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Sign In Screen")
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Place to add additional screens for testing purposes
            }
        }
    }
}