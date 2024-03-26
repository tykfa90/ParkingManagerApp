package com.parkingmanagerapp.view

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.parkingmanagerapp.ui.theme.StandardScreenLayout
import com.parkingmanagerapp.utility.Screen

@Composable
fun TestMenuScreen(navController: NavController) {
    StandardScreenLayout(
        buttons = listOf(
            "SignInScreen" to { navController.navigate(Screen.SignInScreen.route) }
        )
    )
}