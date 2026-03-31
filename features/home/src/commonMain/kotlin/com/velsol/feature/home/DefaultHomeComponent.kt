package com.velsol.feature.home

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TapCountThreshold = 3
private const val TapResetDelayMs = 600L

class DefaultHomeComponent(
    componentContext: ComponentContext,
    private val onShowDemoSwitcher: () -> Unit,
) : HomeComponent,
    ComponentContext by componentContext {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private var tapCount = 0
    private var tapResetJob: Job? = null

    init {
        lifecycle.doOnDestroy { scope.cancel() }
    }

    override fun onLogoTapped() {
        tapCount++
        tapResetJob?.cancel()
        if (tapCount >= TapCountThreshold) {
            onShowDemoSwitcher()
            tapCount = 0
            return
        }
        tapResetJob = scope.launch {
            delay(TapResetDelayMs)
            tapCount = 0
        }
    }
}
