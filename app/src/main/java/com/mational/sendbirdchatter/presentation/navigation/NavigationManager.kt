package com.mational.sendbirdchatter.presentation.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationManager @Inject constructor() {
    private val _navEvents = MutableSharedFlow<NavigationEvent>(replay = 1)
    val navEvents = _navEvents.asSharedFlow()

    fun navigateTo(route: String, isClearPopstack: Boolean = false) {
        _navEvents.tryEmit(NavigationEvent.NavigateTo(route, isClearPopstack))
    }

    fun navigateToChannel(channelUrl: String) {
        _navEvents.tryEmit(NavigationEvent.NavigateToChannel(channelUrl))
    }
}
