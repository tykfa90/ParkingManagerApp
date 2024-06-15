package com.parkingmanagerapp.view.regUserPanel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.parkingmanagerapp.model.UserRole
import com.parkingmanagerapp.ui.theme.StandardScreenLayout
import com.parkingmanagerapp.utility.Screen
import com.parkingmanagerapp.viewModel.AuthViewModel

@Composable
fun MainMenuScreen(navController: NavController, snackbarHostState: SnackbarHostState, viewModel: AuthViewModel = hiltViewModel()) {
    val user = viewModel.user.collectAsState().value
    StandardScreenLayout(
        title = "Main Menu",
        snackbarHostState = snackbarHostState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { navController.navigate(Screen.Reservation.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Parking Reservation")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate(Screen.MyReservations.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("My Reservations")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (user?.role == UserRole.ADMIN) {
                Button(
                    onClick = { navController.navigate(Screen.AdminMenu.route) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Admin Panel")
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            Button(
                onClick = { navController.navigate(Screen.UserProfile.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("User Account")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.signOut()
                    navController.navigate(Screen.SignIn.route) {
                        // Adjust navigation to clear back stack properly
                        popUpTo(Screen.SignIn.route) { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sign Out")
            }
        }
    }
}