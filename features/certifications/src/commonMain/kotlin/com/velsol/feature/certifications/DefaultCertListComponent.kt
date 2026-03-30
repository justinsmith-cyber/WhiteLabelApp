package com.velsol.feature.certifications

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultCertListComponent(
    componentContext: ComponentContext,
    httpClient: HttpClient,
    apiBaseUrl: String,
    private val onCertSelectedCallback: (String) -> Unit,
) : CertListComponent, ComponentContext by componentContext {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private val repository = CertificationsRepository(httpClient, apiBaseUrl)

    private val _certs = MutableStateFlow<List<CertRecord>>(mockCertifications)
    override val certs: StateFlow<List<CertRecord>> = _certs

    private val _isLoading = MutableStateFlow(false)
    override val isLoading: StateFlow<Boolean> = _isLoading

    init {
        lifecycle.doOnDestroy { scope.cancel() }
        loadCertifications()
    }

    private fun loadCertifications() {
        scope.launch {
            _isLoading.value = true
            _certs.value = repository.getCertifications()
            _isLoading.value = false
        }
    }

    override fun onCertSelected(certName: String) {
        onCertSelectedCallback(certName)
    }
}
