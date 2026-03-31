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
import kotlinx.coroutines.launch

class DefaultCertListComponent internal constructor(
    componentContext: ComponentContext,
    private val getCertifications: GetCertificationsUseCase,
    private val onCertSelectedCallback: (String) -> Unit,
) : CertListComponent,
    ComponentContext by componentContext {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _state = MutableStateFlow<CertListState>(CertListState.Loading)
    override val state: StateFlow<CertListState> = _state.asStateFlow()

    init {
        lifecycle.doOnDestroy { scope.cancel() }
        loadCertifications()
    }

    private fun loadCertifications() {
        scope.launch {
            // List state is only Loading vs loaded Content; add refresh-specific state when pull-to-refresh exists.
            val loadedCerts = getCertifications()
            _state.value = CertListState.Content(
                certs = loadedCerts,
                activeCount = loadedCerts.count { it.status == CertStatus.Active },
                expiringCount = loadedCerts.count { it.status == CertStatus.Expiring },
            )
        }
    }

    override fun onIntent(intent: CertListIntent) {
        when (intent) {
            is CertListIntent.SelectCert -> onCertSelectedCallback(intent.certName)
        }
    }
}
