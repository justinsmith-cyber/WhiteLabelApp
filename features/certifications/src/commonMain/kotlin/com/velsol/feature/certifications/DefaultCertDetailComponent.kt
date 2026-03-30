package com.velsol.feature.certifications

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

class DefaultCertDetailComponent internal constructor(
    componentContext: ComponentContext,
    override val certName: String,
    private val onBackCallback: () -> Unit,
    private val getCertification: GetCertificationUseCase,
) : CertDetailComponent,
    ComponentContext by componentContext {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _state = MutableStateFlow(CertDetailState(isLoading = true))
    override val state: StateFlow<CertDetailState> = _state.asStateFlow()

    init {
        lifecycle.doOnDestroy { scope.cancel() }
        loadCertification()
    }

    private fun loadCertification() {
        scope.launch {
            val cert = getCertification(certName)
            _state.update { it.copy(cert = cert, isLoading = false) }
        }
    }

    override fun onBack() {
        onBackCallback()
    }
}
