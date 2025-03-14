package com.mational.sendbirdchatter.presentation.navigation

sealed class Screens(val route: String) {
    data object LoginRoute : Screens("LoginRoute")
    data object RegisterRoute : Screens("RegisterRoute")
    data object NotificationsRoute : Screens("NotificationsRoute")
}