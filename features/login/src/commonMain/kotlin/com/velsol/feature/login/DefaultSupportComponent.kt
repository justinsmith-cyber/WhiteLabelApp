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

class DefaultSupportComponent(
    componentContext: ComponentContext,
    private val supportRepository: SupportRepository,
    private val onBackCallback: () -> Unit,
) : SupportComponent,
    ComponentContext by componentContext {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _state = MutableStateFlow<SupportState>(SupportState.Loading)
    override val state: StateFlow<SupportState> = _state.asStateFlow()

    init {
        lifecycle.doOnDestroy { scope.cancel() }
        loadSupportData()
    }

    private fun loadSupportData() {
        scope.launch {
            _state.value = SupportState.Content(supportRepository.getSupportData())
        }
    }

    override fun onBack() = onBackCallback()
}
