package com.velsol.feature.login

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DefaultLoginScreenComponent(
    componentContext: ComponentContext,
    private val authRepository: AuthRepository,
    private val onLoginSuccessCallback: () -> Unit,
    private val onNavigateToSupportCallback: () -> Unit,
) : LoginScreenComponent,
    ComponentContext by componentContext {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    override val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    private val _rememberName = MutableStateFlow(false)
    override val rememberName: StateFlow<Boolean> = _rememberName.asStateFlow()

    init {
        lifecycle.doOnDestroy { scope.cancel() }
    }

    override fun onIntent(intent: LoginIntent) {
        when (intent) {
            LoginIntent.SignIn -> signIn()
            is LoginIntent.SetRememberName -> _rememberName.value = intent.enabled
            LoginIntent.NavigateToSupport -> onNavigateToSupportCallback()
        }
    }

    private fun signIn() {
        if (_loginState.value is LoginState.Loading) return
        scope.launch {
            _loginState.value = LoginState.Loading
            authRepository.signInWithSso()
                .onSuccess { onLoginSuccessCallback() }
                .onFailure { _loginState.value = LoginState.Error(it.message ?: "Sign-in failed") }
        }
    }
}
