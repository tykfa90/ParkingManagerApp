package com.parkingmanagerapp.view.adminPanel.userManagement

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.parkingmanagerapp.model.User

@Composable
fun DisableUserConfirmationDialog(
    user: User,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val actionText = if (user.active) "Disable" else "Enable"

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("$actionText User") },
        text = {
            Text("Are you sure you want to $actionText this user account: ${user.name} ${user.surname}?")
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}