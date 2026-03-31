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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DefaultLoginScreenComponent(
    componentContext: ComponentContext,
    private val authRepository: AuthRepository,
    private val onLoginSuccessCallback: () -> Unit,
    private val onNavigateToSupportCallback: () -> Unit,
) : LoginScreenComponent,
    ComponentContext by componentContext {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _state = MutableStateFlow(LoginScreenState())
    override val state: StateFlow<LoginScreenState> = _state.asStateFlow()

    init {
        lifecycle.doOnDestroy { scope.cancel() }
    }

    override fun onIntent(intent: LoginIntent) {
        when (intent) {
            LoginIntent.SignIn -> signIn()
            LoginIntent.SignInWithCredentials -> signInWithCredentials()
            is LoginIntent.SetUsername -> _state.update { it.copy(username = intent.username, error = null) }
            is LoginIntent.SetPassword -> _state.update { it.copy(password = intent.password, error = null) }
            is LoginIntent.SetRememberName -> _state.update { it.copy(rememberName = intent.enabled) }
            LoginIntent.NavigateToSupport -> onNavigateToSupportCallback()
        }
    }

    private fun signIn() {
        if (_state.value.isLoading) return
        scope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            authRepository.signInWithSso()
                .onSuccess { onLoginSuccessCallback() }
                .onFailure { _state.update { s -> s.copy(isLoading = false, error = LoginError.SignInFailed) } }
        }
    }

    private fun signInWithCredentials() {
        if (_state.value.isLoading) return
        if (_state.value.username.isBlank() || _state.value.password.isBlank()) {
            _state.update { it.copy(error = LoginError.CredentialsRequired) }
            return
        }
        scope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            authRepository.signInWithUsernamePassword(_state.value.username, _state.value.password)
                .onSuccess { onLoginSuccessCallback() }
                .onFailure { _state.update { s -> s.copy(isLoading = false, error = LoginError.SignInFailed) } }
        }
    }
}
