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

class DefaultCertListComponent internal constructor(
    componentContext: ComponentContext,
    private val getCertifications: GetCertificationsUseCase,
    private val onCertSelectedCallback: (String) -> Unit,
) : CertListComponent,
    ComponentContext by componentContext {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _state = MutableStateFlow(
        CertListState(
            certs = mockCertifications,
            isLoading = false,
            activeCount = mockCertifications.count { it.status == CertStatus.Active },
            expiringCount = mockCertifications.count { it.status == CertStatus.Expiring },
        ),
    )
    override val state: StateFlow<CertListState> = _state.asStateFlow()

    init {
        lifecycle.doOnDestroy { scope.cancel() }
        loadCertifications()
    }

    private fun loadCertifications() {
        scope.launch {
            _state.update { it.copy(isLoading = true) }
            val loadedCerts = getCertifications()
            _state.update {
                it.copy(
                    certs = loadedCerts,
                    isLoading = false,
                    activeCount = loadedCerts.count { cert -> cert.status == CertStatus.Active },
                    expiringCount = loadedCerts.count { cert -> cert.status == CertStatus.Expiring },
                )
            }
        }
    }

    override fun onCertSelected(certName: String) {
        onCertSelectedCallback(certName)
    }
}
