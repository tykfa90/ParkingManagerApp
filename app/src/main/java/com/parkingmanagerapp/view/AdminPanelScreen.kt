package com.parkingmanagerapp.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.parkingmanagerapp.ui.theme.StandardScreenLayout

@Composable
fun AdminPanelScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState
) {
    StandardScreenLayout(title = "Admin Panel", snackbarHostState = snackbarHostState) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TODO("Implement ADMIN account functionalities and include on a list of buttons in here.")
        }
    }
}