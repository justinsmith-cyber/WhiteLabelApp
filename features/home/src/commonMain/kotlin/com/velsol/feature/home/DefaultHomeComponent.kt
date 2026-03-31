package com.velsol.feature.home

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TapCountThreshold = 3
private const val TapResetDelayMs = 600L

class DefaultHomeComponent(
    componentContext: ComponentContext,
    private val onShowDemoSwitcher: () -> Unit,
) : HomeComponent,
    ComponentContext by componentContext {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _state = MutableStateFlow(HomeState())
    override val state: StateFlow<HomeState> = _state.asStateFlow()

    private var tapResetJob: Job? = null

    init {
        lifecycle.doOnDestroy { scope.cancel() }
    }

    override fun onIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.LogoTapped -> handleLogoTapped()
        }
    }

    private fun handleLogoTapped() {
        val newCount = _state.value.tapCount + 1
        tapResetJob?.cancel()
        if (newCount >= TapCountThreshold) {
            _state.value = HomeState(tapCount = 0)
            onShowDemoSwitcher()
            return
        }
        _state.value = HomeState(tapCount = newCount)
        tapResetJob = scope.launch {
            delay(TapResetDelayMs)
            _state.value = HomeState(tapCount = 0)
        }
    }
}
