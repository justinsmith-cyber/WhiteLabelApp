package com.velsol.feature.certifications

import kotlinx.coroutines.flow.StateFlow

interface CertListComponent {
    val certs: StateFlow<List<CertRecord>>
    val isLoading: StateFlow<Boolean>
    fun onCertSelected(certName: String)
}
