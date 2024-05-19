package com.parkingmanagerapp.view.adminPanel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.parkingmanagerapp.ui.theme.StandardScreenLayout
import com.parkingmanagerapp.utility.Screen

@Composable
fun AdminPanelScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState
) {
    StandardScreenLayout(title = "Administrator Panel", snackbarHostState = snackbarHostState) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { navController.navigate(Screen.AdminMenuParkingSlots.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Manage Parking Slots")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate(Screen.AdminMenuUserAccounts.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Manage User Accounts")
            }
        }
    }
}