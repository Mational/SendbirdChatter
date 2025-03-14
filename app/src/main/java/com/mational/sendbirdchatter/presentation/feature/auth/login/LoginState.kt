package com.mational.sendbirdchatter.presentation.feature.auth.login

sealed class LoginState {
    data class FillFormState(
        val email: String = "",
        val password: String = "",
        val errorMessage: String? = null
    ) : LoginState()

    data object LoadingState : LoginState()
}
