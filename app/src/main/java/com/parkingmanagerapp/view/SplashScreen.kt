package com.parkingmanagerapp.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.parkingmanagerapp.viewModel.AuthViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(navController: NavController, authViewModel: AuthViewModel) {
    var isReadyToNavigate by remember { mutableStateOf(false) }
    val isUserAuthenticated by authViewModel.isUserAuthenticated.collectAsState(initial = null)

    LaunchedEffect(key1 = true) {
        // Wait for the longest of the available conditions
        coroutineScope {
            val delayJob = launch { delay(3000) } // Minimal waiting time
            delayJob.join() // Ensure the delay completes

            // Checking if the navigation is ready to proceed
            isReadyToNavigate = true
        }
    }

    // Check whether the isReadyToNavigate flag is set to true
    if (isReadyToNavigate) {
        LaunchedEffect(key1 = isUserAuthenticated) {
            when (isUserAuthenticated) {
                true -> navController.navigate("home") {
                    popUpTo("splash") { inclusive = true }
                    launchSingleTop = true
                }
                false -> navController.navigate("signIn") {
                    popUpTo("splash") { inclusive = true }
                    launchSingleTop = true
                }
                null -> {} // Handle the initial or loading state if needed
            }
        }
    }

    // UI for the splash screen
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Parking Manager App",
                fontSize = 24.sp,
                modifier = Modifier.padding(top = 16.dp),
                fontFamily = FontFamily.SansSerif,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Loading...",
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}