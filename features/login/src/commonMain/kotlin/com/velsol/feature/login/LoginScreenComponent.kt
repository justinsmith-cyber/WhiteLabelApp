package com.velsol.feature.login

import kotlinx.coroutines.flow.StateFlow

data class LoginScreenState(
    val username: String = "",
    val password: String = "",
    val rememberName: Boolean = false,
    val isLoading: Boolean = false,
    val error: LoginError? = null,
)

sealed interface LoginError {
    data object CredentialsRequired : LoginError
    data object SignInFailed : LoginError
}

sealed interface LoginIntent {
    data object SignIn : LoginIntent
    data object SignInWithCredentials : LoginIntent
    data class SetUsername(val username: String) : LoginIntent
    data class SetPassword(val password: String) : LoginIntent
    data class SetRememberName(val enabled: Boolean) : LoginIntent
    data object NavigateToSupport : LoginIntent
}

interface LoginScreenComponent {
    val state: StateFlow<LoginScreenState>

    fun onIntent(intent: LoginIntent)
}
