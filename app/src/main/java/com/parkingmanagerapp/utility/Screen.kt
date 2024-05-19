package com.parkingmanagerapp.utility

sealed class Screen(val route: String) {
    data object SplashScreen : Screen("splash_screen")
    data object SignIn : Screen("sign_in")
    data object Register : Screen("register")
    data object Home : Screen("home")
    data object ParkingSlots : Screen("parking_slots")
    data object AdminMenu : Screen("admin_menu")
    data object AdminMenuParkingSlots : Screen("admin_parking_slots")
    data object AdminMenuUserAccounts : Screen("admin_user_accounts")
    data object EditUserProfile : Screen("edit_user_profile")
    data object UserProfile : Screen("user_profile")
}