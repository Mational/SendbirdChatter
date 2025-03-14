package com.mational.sendbirdchatter.presentation.feature.auth.register

sealed class RegisterState {
    data class FillFormState(
        val email: String = "",
        val password: String = "",
        val confirmPassword: String = "",
        val errorMessage: String? = null
    ): RegisterState()
    data object LoadingState: RegisterState()
}