package com.velsol.feature.certifications

import kotlinx.coroutines.flow.StateFlow

sealed interface CertListState {
    data object Loading : CertListState

    data class Content(
        val certs: List<CertRecord>,
        val activeCount: Int,
        val expiringCount: Int,
        val isRefreshing: Boolean = false,
    ) : CertListState
}

sealed interface CertListIntent {
    data class SelectCert(val certName: String) : CertListIntent
}

interface CertListComponent {
    val state: StateFlow<CertListState>

    fun onIntent(intent: CertListIntent)
}
