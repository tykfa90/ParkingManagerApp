package com.parkingmanagerapp.view.regUserPanel.userOwnAccountManagemenet

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.parkingmanagerapp.viewModel.AuthViewModel

@Composable
fun EditEmailDialog(
    onDismiss: () -> Unit,
    snackbarHostState: SnackbarHostState,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val activity = context as Activity
    val currentEmail = viewModel.user.value?.email ?: ""
    var newEmail by remember { mutableStateOf(currentEmail) }
    var password by remember { mutableStateOf("") }
    var verificationCode by remember { mutableStateOf("") }
    val verificationId by viewModel.verificationId.collectAsState()
    val useTestPhoneNumber by viewModel.useTestPhoneNumber.collectAsState()
    val phoneNumber = viewModel.user.value?.phoneNumber ?: ""

    LaunchedEffect(viewModel.snackbarMessage) {
        viewModel.snackbarMessage.value?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearSnackbarMessage()
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Edit Email") },
        text = {
            Column {
                OutlinedTextField(
                    value = newEmail,
                    onValueChange = { newEmail = it },
                    label = { Text(text = "New Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(text = "Password") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )
                if (useTestPhoneNumber && phoneNumber == "+48 111111111") {
                    Text("Using test phone number. Enter verification code: 111111")
                }
                OutlinedTextField(
                    value = verificationCode,
                    onValueChange = { verificationCode = it },
                    label = { Text(text = "Verification Code") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !(useTestPhoneNumber && phoneNumber == "+48 111111111")
                )
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = useTestPhoneNumber,
                        onCheckedChange = { viewModel.setUseTestPhoneNumber(it) }
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Use Test Phone Number")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (verificationId != null || (useTestPhoneNumber && phoneNumber == "+48 111111111")) {
                        viewModel.updateUserPhoneNumber(password, phoneNumber, verificationId ?: "test-verification-id", verificationCode)
                        viewModel.updateUserEmail(password, newEmail)
                    } else {
                        viewModel.sendVerificationCode(phoneNumber, activity)
                    }
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Update Email")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}