package com.velsol.feature.login

import kotlinx.coroutines.flow.StateFlow

sealed interface LoginState {
    data object Idle : LoginState
    data object Loading : LoginState
    data class Error(val message: String) : LoginState
}

sealed interface LoginIntent {
    data object SignIn : LoginIntent
    data class SetRememberName(val enabled: Boolean) : LoginIntent
    data object NavigateToSupport : LoginIntent
}

interface LoginScreenComponent {
    val loginState: StateFlow<LoginState>
    val rememberName: StateFlow<Boolean>

    fun onIntent(intent: LoginIntent)
}
