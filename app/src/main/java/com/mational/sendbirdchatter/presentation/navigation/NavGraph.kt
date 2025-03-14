package com.mational.sendbirdchatter.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mational.sendbirdchatter.presentation.feature.auth.login.LoginScreen
import com.mational.sendbirdchatter.presentation.feature.auth.register.RegisterScreen
import com.mational.sendbirdchatter.presentation.feature.notifications.NotificationsScreen
import com.sendbird.uikit.compose.channels.group.detail.ChannelScreen
import com.sendbird.uikit.compose.channels.group.list.ChannelsScreen
import com.sendbird.uikit.compose.channels.group.list.ChannelsTopBar
import com.sendbird.uikit.compose.channels.group.members.MembersScreen
import com.sendbird.uikit.compose.channels.group.members.MembersTopBar
import com.sendbird.uikit.compose.navigation.SendbirdNavigation
import com.sendbird.uikit.compose.navigation.sendbirdGroupChannelNavGraph
import java.net.URLEncoder

@Composable
fun Nav(navigationManager: NavigationManager) {
    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        navigationManager.navEvents.collect { event ->
            when (event) {
                is NavigationEvent.NavigateToChannel -> {
                    val encodedUrl = URLEncoder.encode(event.channelUrl, "UTF-8")
                    val route = "sb_route_channel/$encodedUrl"
                    navController.navigate(route) { popUpTo(0) { inclusive = true } }
                }
                is NavigationEvent.NavigateTo -> {
                    if (event.isClearPopstack) {
                        navController.navigate(event.route) { popUpTo(0) { inclusive = true } }
                    } else {
                        navController.navigate(event.route)
                    }
                }
            }
        }
    }

    HandleGlobalBackButton(navController)

    NavHost(navController = navController, startDestination = Screens.LoginRoute.route) {
        composable(Screens.LoginRoute.route) {
            LoginScreen()
        }

        composable(Screens.RegisterRoute.route) {
            RegisterScreen()
        }

        composable(Screens.NotificationsRoute.route) {
            NotificationsScreen()
        }

        sendbirdGroupChannelNavGraph(
            navController = navController,
            channelsScreen = {
                CustomChannelsScreen(navController)
            },
            channelScreen = { channelUrl->
                CustomChannelScreen(navController, channelUrl)
            },
            membersScreen = { channelUrl ->
                CustomMembersScreen(navController, channelUrl)
            }
        )
    }
}

@Composable
fun CustomChannelsScreen(navController: NavController) {
    ChannelsScreen(
        navController = navController,
        topBar = { _, onActionClick ->
            ChannelsTopBar(
                onActionClick = onActionClick,
                onNavigationIconClick = {},
                navigationIcon = {}
            )
        }
    )
}

@Composable
fun CustomChannelScreen(navController: NavController, channelUrl: String) {
    ChannelScreen(
        navController = navController,
        channelUrl = channelUrl,
        onTopBarNavigationIconClick = {
            navController.navigate(SendbirdNavigation.GroupChannel.route)
        }
    )
}

@Composable
fun CustomMembersScreen(navController: NavController, channelUrl: String) {
    MembersScreen(
        navController = navController,
        channelUrl = channelUrl,
        topBar = { onNavigationIconClick, _ ->
            MembersTopBar(
                onNavigationIconClick = onNavigationIconClick,
                onActionClick = {},
                action = {}
            )
        }
    )
}