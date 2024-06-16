package com.parkingmanagerapp.view.regUserPanel.userOwnAccountManagement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.parkingmanagerapp.viewModel.AuthViewModel

@Composable
fun EditNameDialog(
    onDismiss: () -> Unit,
    snackbarHostState: SnackbarHostState,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var newName by remember { mutableStateOf("") }

    LaunchedEffect(viewModel.snackbarMessage) {
        viewModel.snackbarMessage.value?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearSnackbarMessage()
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Edit Name") },
        text = {
            Column {
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text(text = "New Name") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    viewModel.updateUserName(newName)
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Update Name")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}