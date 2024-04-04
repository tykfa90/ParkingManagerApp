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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.parkingmanagerapp.ui.theme.ParkingManagerAppTheme
import com.parkingmanagerapp.ui.theme.StandardScreenLayout
import com.parkingmanagerapp.utility.Screen
import com.parkingmanagerapp.viewModel.AuthViewModel

@Composable
fun TestMenuScreen(navController: NavController, viewModel: AuthViewModel = hiltViewModel()) {
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

                Button(
                    onClick = {
                        viewModel.signOut()
                        navController.navigate(Screen.SignIn.route) {
                            popUpTo(0)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Sign Out")
                }

                // Add additional buttons/screens for testing purposes as needed
            }
        }
    }
}