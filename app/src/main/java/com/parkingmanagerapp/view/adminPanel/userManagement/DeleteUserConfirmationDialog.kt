package com.parkingmanagerapp.view.adminPanel.userManagement

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.parkingmanagerapp.model.User

@Composable
fun DeleteUserConfirmationDialog(
    user: User,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Delete User") },
        text = {
            Column {
                Text(
                    text = "Are you sure you want to delete this account?",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(text = "Name: ${user.name} ${user.surname}")
                Text(text = "Email: ${user.email}")
            }
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Delete")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}