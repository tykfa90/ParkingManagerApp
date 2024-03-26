package com.parkingmanagerapp.utility

sealed class Screen(val route: String) {
    data object MainMenu : Screen("mainMenu")
    data object ParkingSlots : Screen("parkingSlots")
    data object TestMenu : Screen("testMenu")
}