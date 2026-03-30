package com.velsol.feature.certifications

import kotlinx.coroutines.flow.StateFlow

data class CertListState(
    val certs: List<CertRecord> = emptyList(),
    val isLoading: Boolean = false,
    val activeCount: Int = 0,
    val expiringCount: Int = 0,
)

interface CertListComponent {
    val state: StateFlow<CertListState>
    fun onCertSelected(certName: String)
}
