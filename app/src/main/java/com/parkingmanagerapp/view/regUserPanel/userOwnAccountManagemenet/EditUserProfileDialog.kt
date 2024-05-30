import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.parkingmanagerapp.viewModel.AuthViewModel

@Composable
fun EditUserProfileDialog(
    onDismiss: () -> Unit,
    snackbarHostState: SnackbarHostState,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val user by viewModel.user.collectAsState()
    val verificationId by viewModel.verificationId.collectAsState()
    val context = LocalContext.current
    val activity = context as? Activity

    var name by remember { mutableStateOf(user?.name ?: "") }
    var surname by remember { mutableStateOf(user?.surname ?: "") }
    var phoneNumber by remember { mutableStateOf(user?.phoneNumber ?: "") }
    var email by remember { mutableStateOf(user?.email ?: "") }
    var password by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }

    // Observe updates and display snackbar messages
    LaunchedEffect(key1 = viewModel.snackbarMessage) {
        viewModel.snackbarMessage.value?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearSnackbarMessage()
        }
    }

    LaunchedEffect(user) {
        name = user?.name ?: ""
        surname = user?.surname ?: ""
        phoneNumber = user?.phoneNumber ?: ""
        email = user?.email ?: ""
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Edit Profile") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = surname,
                    onValueChange = { surname = it },
                    label = { Text("Surname") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Phone Number") },
                    modifier = Modifier.fillMaxWidth()
                )
                if (verificationId != null) {
                    OutlinedTextField(
                        value = code,
                        onValueChange = { code = it },
                        label = { Text("Verification Code") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (phoneNumber != user?.phoneNumber) {
                        // Test phone number logic for development purposes
                        if (phoneNumber == "+48111111111") {
                            activity?.let {
                                viewModel.signInWithTestPhoneNumber(phoneNumber, it)
                            }
                        } else {
                            activity?.let {
                                viewModel.sendVerificationCode(phoneNumber, it)
                            }
                        }
                    } else {
                        viewModel.updateUserProfile(
                            name = name,
                            surname = surname,
                            phoneNumber = phoneNumber,
                            email = email,
                            password = password
                        )
                        onDismiss()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Update Profile")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}