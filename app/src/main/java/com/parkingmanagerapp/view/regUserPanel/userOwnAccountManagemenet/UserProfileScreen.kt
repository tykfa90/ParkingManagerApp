package com.parkingmanagerapp.view.regUserPanel.userOwnAccountManagemenet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
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
    var showDialog by remember { mutableStateOf(false) }
    var attributeToEdit by remember { mutableStateOf("") }
    var currentValue by remember { mutableStateOf("") }

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
                onEditClick = {
                    attributeToEdit = "Name"
                    currentValue = user?.name ?: ""
                    showDialog = true
                }
            )
            UserAttribute(
                label = "Surname",
                value = user?.surname ?: "",
                onEditClick = {
                    attributeToEdit = "Surname"
                    currentValue = user?.surname ?: ""
                    showDialog = true
                }
            )
            UserAttribute(
                label = "Email",
                value = user?.email ?: "",
                onEditClick = {
                    attributeToEdit = "Email"
                    currentValue = user?.email ?: ""
                    showDialog = true
                }
            )
            UserAttribute(
                label = "Phone Number",
                value = user?.phoneNumber ?: "",
                onEditClick = {
                    attributeToEdit = "Phone Number"
                    currentValue = user?.phoneNumber ?: ""
                    showDialog = true
                }
            )
            UserAttribute(
                label = "User access level",
                value = user?.role.toString(),
                onEditClick = null // Role might not be editable
            )
        }
    }

    if (showDialog) {
        EditAttributeDialog(
            attribute = attributeToEdit,
            currentValue = currentValue,
            onDismiss = { showDialog = false },
            onConfirm = { newValue ->
                viewModel.updateUserAttribute(attributeToEdit, newValue)
                showDialog = false
            }
        )
    }
}