package com.mational.sendbirdchatter.presentation.feature.auth.register

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
class RegisterViewModel @Inject constructor(
    private val authManager: AuthManager,
    private val navigationManager: NavigationManager,
    private val sendBirdRepository: SendBirdRepository
): ViewModel() {
    private val _uiState = MutableStateFlow<RegisterState>(RegisterState.FillFormState())
    val uiState: StateFlow<RegisterState> = _uiState

    fun onEmailValueChange(newEmail: String) {
        _uiState.value = (_uiState.value as RegisterState.FillFormState).copy(email = newEmail)
    }

    fun onPasswordValueChange(newPassword: String) {
        _uiState.value = (_uiState.value as RegisterState.FillFormState).copy(password = newPassword)
    }

    fun onConfirmPasswordValueChange(confirmedPassword: String) {
        _uiState.value = (_uiState.value as RegisterState.FillFormState).copy(confirmPassword = confirmedPassword)
    }

    fun onRegisterButtonClicked() {
        viewModelScope.launch {
            val oldState = (_uiState.value as RegisterState.FillFormState)
            _uiState.value = RegisterState.LoadingState
            val registerResult = authManager.createAccount(oldState.email, oldState.password)
            registerResult.onSuccess { userId ->
                val createSendBirdUserResult = sendBirdRepository.createSendBirdUser(userId, oldState.email)
                createSendBirdUserResult.onSuccess {
                    navigateTo(Screens.LoginRoute.route)
                }.onFailure {
                    _uiState.value = RegisterState.FillFormState(errorMessage = "Failed to create user account")
                }
            }
            registerResult.onFailure {
                _uiState.value = RegisterState.FillFormState(errorMessage = "Registration failed")
            }
        }
    }

    fun isRegisterButtonEnabled(): Boolean {
        val state = _uiState.value as RegisterState.FillFormState
        return state.email.isNotBlank() && state.password.isNotBlank() && state.confirmPassword.isNotBlank()
    }


    fun clearError() {
        _uiState.value = (_uiState.value as RegisterState.FillFormState).copy(errorMessage = null)
    }

    fun navigateTo(route: String) {
        navigationManager.navigateTo(route)
    }
}