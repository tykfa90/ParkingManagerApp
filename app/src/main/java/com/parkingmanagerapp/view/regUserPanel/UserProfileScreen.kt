package com.parkingmanagerapp.view.regUserPanel

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.parkingmanagerapp.ui.theme.StandardScreenLayout
import com.parkingmanagerapp.utility.Screen
import com.parkingmanagerapp.viewModel.AuthViewModel

@Composable
fun UserProfileScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState
) {
    val user by viewModel.user.collectAsState()

    // Observe updates and display snackbar messages
    LaunchedEffect(key1 = viewModel.snackbarMessage) {
        viewModel.snackbarMessage.value?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearSnackbarMessage()
        }
    }

    StandardScreenLayout(title = "User Account", snackbarHostState = snackbarHostState) {
        Column {
            Text("User Details", style = MaterialTheme.typography.titleLarge)
            Text("Name: ${user?.name}")
            Text("Surname: ${user?.surname}")
            Text("Email: ${user?.email}")
            Text("User access level: ${user?.role}")
            Button(onClick = {
                navController.navigate(Screen.EditUserProfile.route)
            }) {
                Text("Edit Profile")
            }
        }
    }
}