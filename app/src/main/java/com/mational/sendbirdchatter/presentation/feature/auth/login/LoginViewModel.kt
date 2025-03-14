package com.mational.sendbirdchatter.presentation.feature.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mational.sendbirdchatter.data.firebase.AuthManager
import com.mational.sendbirdchatter.domain.repository.SendBirdRepository
import com.mational.sendbirdchatter.presentation.navigation.NavigationManager
import com.mational.sendbirdchatter.presentation.navigation.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authManager: AuthManager,
    private val sendBirdRepository: SendBirdRepository,
    private val navigationManager: NavigationManager
): ViewModel() {
    private val _uiState = MutableStateFlow<LoginState>(LoginState.FillFormState())
    val uiState: StateFlow<LoginState> = _uiState

    fun onEmailValueChange(newEmail: String) {
        _uiState.value = (_uiState.value as LoginState.FillFormState).copy(email = newEmail)
    }

    fun onPasswordValueChange(newPassword: String) {
        _uiState.value = (_uiState.value as LoginState.FillFormState).copy(password = newPassword)
    }

    fun onLoginButtonClicked() {
        viewModelScope.launch {
            val oldState = (_uiState.value as LoginState.FillFormState)
            _uiState.value = LoginState.LoadingState
            val signInResult = authManager.signIn(oldState.email, oldState.password)
            signInResult.onSuccess { userId ->
                val getSendBirdUserResult = sendBirdRepository.getSendBirdUser(userId, oldState.email)
                getSendBirdUserResult.onSuccess { sendBirdUser ->
                    sendBirdRepository.prepareSendBird(sendBirdUser)
                    navigateTo(Screens.NotificationsRoute.route)
                }.onFailure {
                    _uiState.value = LoginState.FillFormState(errorMessage = "Failed to get user account")
                }
            }.onFailure {
                _uiState.value = LoginState.FillFormState(errorMessage = "Login failed")
            }
        }
    }

    fun isLoginButtonEnabled(): Boolean {
        val state = _uiState.value as LoginState.FillFormState
        return state.email.isNotBlank() && state.password.isNotBlank()
    }
    fun clearError() {
        _uiState.value = (_uiState.value as LoginState.FillFormState).copy(errorMessage = null)
    }

    fun navigateTo(route: String) {
        navigationManager.navigateTo(route)
    }
}