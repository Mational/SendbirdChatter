package com.mational.sendbirdchatter.presentation.feature.notifications

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.mational.sendbirdchatter.data.model.RegistrationToken
import com.mational.sendbirdchatter.domain.repository.SendBirdRepository
import com.mational.sendbirdchatter.presentation.navigation.NavigationManager
import com.sendbird.uikit.compose.navigation.SendbirdNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val sendBirdRepository: SendBirdRepository,
    private val navigationManager: NavigationManager,
    private val registrationToken: RegistrationToken
): ViewModel() {
    fun onPermissionsGranted() {
        viewModelScope.launch {
            try {
                val token = registrationToken.getToken()
                val currentUser = FirebaseAuth.getInstance().currentUser

                if (currentUser != null && token != null) {
                    val result = sendBirdRepository.registerPushToken(currentUser.uid, token)
                    result.onSuccess {
                        navigateTo(SendbirdNavigation.GroupChannel.route)
                    }.onFailure {
                        navigateTo(SendbirdNavigation.GroupChannel.route)
                    }
                }

            } catch (e: Exception) {
                navigateTo(SendbirdNavigation.GroupChannel.route)
            }
        }
    }

    fun onPermissionDenied() {
        navigateTo(SendbirdNavigation.GroupChannel.route)
    }

    fun isNotificationPermissionGranted(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    fun navigateTo(route: String) {
        navigationManager.navigateTo(route, true)
    }
}