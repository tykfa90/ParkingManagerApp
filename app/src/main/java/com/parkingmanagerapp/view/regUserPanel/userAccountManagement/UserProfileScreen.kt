package com.parkingmanagerapp.view.regUserPanel.userAccountManagement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.parkingmanagerapp.model.UserRole
import com.parkingmanagerapp.ui.theme.StandardScreenLayout
import com.parkingmanagerapp.viewModel.AuthViewModel

@Composable
fun UserProfileScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState
) {
    val user by viewModel.user.collectAsState()
    var showEditEmailDialog by remember { mutableStateOf(false) }
    var showEditPasswordDialog by remember { mutableStateOf(false) }
    var showEditPhoneNumberDialog by remember { mutableStateOf(false) }
    var showEditNameDialog by remember { mutableStateOf(false) }
    var showEditSurnameDialog by remember { mutableStateOf(false) }

    // Observe updates and display snackbar messages
    LaunchedEffect(viewModel.snackbarMessage) {
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
            UserAttribute(
                label = "Name",
                value = user?.name ?: "",
                onEditClick = { showEditNameDialog = true }
            )
            UserAttribute(
                label = "Surname",
                value = user?.surname ?: "",
                onEditClick = { showEditSurnameDialog = true }
            )
            UserAttribute(
                label = "Email",
                value = user?.email ?: "",
                onEditClick = { showEditEmailDialog = true }
            )
            UserAttribute(
                label = "Phone Number",
                value = user?.phoneNumber ?: "",
                onEditClick = { showEditPhoneNumberDialog = true }
            )
            if (user?.role == UserRole.ADMIN) {
                UserAttribute(
                    label = "User access level",
                    value = user?.role.toString(),
                    onEditClick = null
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { showEditPasswordDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Change Password")
            }
        }
    }

    if (showEditEmailDialog) {
        EditEmailDialog(
            onDismiss = { showEditEmailDialog = false },
            snackbarHostState = snackbarHostState
        )
    }
    if (showEditPasswordDialog) {
        EditPasswordDialog(
            onDismiss = { showEditPasswordDialog = false },
            snackbarHostState = snackbarHostState
        )
    }
    if (showEditPhoneNumberDialog) {
        EditPhoneNumberDialog(
            onDismiss = { showEditPhoneNumberDialog = false },
            snackbarHostState = snackbarHostState
        )
    }
    if (showEditNameDialog) {
        EditNameDialog(
            onDismiss = { showEditNameDialog = false },
            snackbarHostState = snackbarHostState
        )
    }
    if (showEditSurnameDialog) {
        EditSurnameDialog(
            onDismiss = { showEditSurnameDialog = false },
            snackbarHostState = snackbarHostState
        )
    }
}

@Composable
fun UserAttribute(
    label: String,
    value: String,
    onEditClick: (() -> Unit)?
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(text = "$label: $value", modifier = Modifier.padding(bottom = 4.dp))
        onEditClick?.let {
            Button(onClick = it, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Edit $label")
            }
        }
    }
}