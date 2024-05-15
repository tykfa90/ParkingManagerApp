package com.parkingmanagerapp.utility

sealed class Screen(val route: String) {
    data object SplashScreen : Screen("splash_screen")
    data object SignIn : Screen("sign_in")
    data object Register : Screen("register")
    data object Home : Screen("home")
    data object ParkingSlots : Screen("parking_slots")
    data object TestMenu : Screen("test_menu")
    data object UserProfile : Screen("user_profile")
    data object EditUserProfile : Screen("edit_user_profile")
    data object AdminMenu : Screen("admin_menu")
    data object AdminMenuUserAccounts : Screen("admin_menu_user_accounts")
    data object AdminParkingSlots : Screen("admin_parking_slots")
}