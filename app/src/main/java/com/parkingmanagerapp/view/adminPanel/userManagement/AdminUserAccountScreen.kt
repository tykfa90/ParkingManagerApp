package com.parkingmanagerapp.view.adminPanel.userManagement

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
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
import androidx.navigation.NavController
import com.parkingmanagerapp.model.User
import com.parkingmanagerapp.ui.theme.StandardScreenLayout
import com.parkingmanagerapp.viewModel.AuthViewModel

@Suppress("UNUSED_PARAMETER")
@Composable
fun AdminUserAccountScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val users by viewModel.users.collectAsState()
    var selectedUser by remember { mutableStateOf<User?>(null) }
    var showDisableDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.fetchAllUsers()
    }

    StandardScreenLayout(
        title = "Manage User Accounts",
        snackbarHostState = snackbarHostState
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { viewModel.sortUsersByFirstName() }) {
                    Text("Sort by Name")
                }
                Button(onClick = { viewModel.sortUsersBySurname() }) {
                    Text("Sort by Surname")
                }
            }

            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(users) { user ->
                    UserItem(
                        user = user,
                        onDisableOrEnable = {
                            selectedUser = user
                            showDisableDialog = true
                        }
                    )
                }
            }
        }

        // Disable Confirmation Dialog
        if (showDisableDialog && selectedUser != null) {
            DisableUserConfirmationDialog(
                user = selectedUser!!,
                onDismiss = { showDisableDialog = false },
                onConfirm = {
                    if (selectedUser!!.active) {
                        viewModel.deactivateUser(selectedUser!!.uid)
                    } else {
                        viewModel.activateUser(selectedUser!!.uid)
                    }
                    showDisableDialog = false
                }
            )
        }
    }
}