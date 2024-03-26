package com.parkingmanagerapp.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.parkingmanagerapp.ui.theme.StandardScreenLayout
import com.parkingmanagerapp.viewModel.AuthViewModel

@Composable
fun SignInScreen(navController: NavController, viewModel: AuthViewModel = hiltViewModel()) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) } // State to toggle password visibility
    val signInStatus by viewModel.signInStatus.collectAsState()

    StandardScreenLayout(title = "Sign In") {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon: ImageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = icon, contentDescription = if (passwordVisible) "Hide password" else "Show password")
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.signIn(email, password) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sign In")
            }

            if (signInStatus == false) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Sign in failed. Please try again.", color = MaterialTheme.colorScheme.error)
            }
        }
    }

    LaunchedEffect(signInStatus) {
        if (signInStatus == true) {
            navController.navigate("home") {
                popUpTo("sign_in") { inclusive = true }
            }
        }
    }
}