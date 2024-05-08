package com.parkingmanagerapp.utility

sealed class Screen(val route: String) {
    data object SplashScreen : Screen("splashScreen")
    data object Home : Screen("home")
    data object ParkingSlots : Screen("parkingSlots")
    data object TestMenu : Screen("testMenu")
    data object SignIn : Screen("signIn")
    data object Register : Screen("register")
    data object UserProfile : Screen("userProfile")
    data object EditUserProfile : Screen("editProfile")
    data object AdminMenu : Screen("adminMenu")
}