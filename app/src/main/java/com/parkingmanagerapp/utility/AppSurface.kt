package com.parkingmanagerapp.utility

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

// Overlay element to hold entire application's scaffold and cross-screen elements display
@Composable
fun AppSurface(navController: NavHostController, snackbarHostState: SnackbarHostState) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { contentPadding ->  // The padding values are here if you need them
            Box(modifier = Modifier.padding(contentPadding)) {
                AppNavHost(navController = navController, snackbarHostState = snackbarHostState)
            }
        }
    }
}