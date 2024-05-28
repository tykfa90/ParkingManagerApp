package com.parkingmanagerapp.view.regUserPanel.userOwnAccountManagemenet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.parkingmanagerapp.ui.theme.StandardScreenLayout
import com.parkingmanagerapp.viewModel.AuthViewModel

@Composable
fun UserProfileScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState
) {
    val user by viewModel.user.collectAsState()
    var showEditDialog by remember { mutableStateOf(false) }

    // Observe updates and display snackbar messages
    LaunchedEffect(key1 = viewModel.snackbarMessage) {
        viewModel.snackbarMessage.value?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearSnackbarMessage()
        }
    }

    StandardScreenLayout(title = "User Account", snackbarHostState = snackbarHostState) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("User Details", style = MaterialTheme.typography.titleLarge)
            Text("Name: ${user?.name}")
            Text("Surname: ${user?.surname}")
            Text("Email: ${user?.email}")
            Text("User access level: ${user?.role}")
            Button(onClick = {
                showEditDialog = true
            }) {
                Text("Edit Profile")
            }
        }

        // Show edit dialog
        if (showEditDialog) {
            EditUserProfileDialog(
                onDismiss = { showEditDialog = false },
                snackbarHostState = snackbarHostState,
                viewModel = viewModel
            )
        }
    }
}