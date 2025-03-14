package com.mational.sendbirdchatter.presentation.navigation

sealed class NavigationEvent {
    data class NavigateToChannel(val channelUrl: String) : NavigationEvent()
    data class NavigateTo(val route: String, val isClearPopstack: Boolean = false): NavigationEvent()
}