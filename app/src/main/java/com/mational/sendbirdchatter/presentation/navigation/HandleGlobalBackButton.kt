package com.mational.sendbirdchatter.presentation.navigation

import androidx.activity.addCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController

@Composable
fun HandleGlobalBackButton(navController: NavController) {
    val backPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner, navController) {
        val callback =
            backPressedDispatcher?.addCallback(lifecycleOwner) {
                if (navController.previousBackStackEntry != null) {
                    navController.popBackStack()
                }
            }

        onDispose {
            callback?.remove()
        }
    }
}
