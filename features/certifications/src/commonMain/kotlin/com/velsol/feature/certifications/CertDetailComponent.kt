package com.velsol.feature.certifications

import kotlinx.coroutines.flow.StateFlow

data class CertDetailState(
    val cert: CertRecord? = null,
    val isLoading: Boolean = false,
)

sealed interface CertDetailIntent {
    data object Back : CertDetailIntent
}

interface CertDetailComponent {
    val certName: String
    val state: StateFlow<CertDetailState>

    fun onIntent(intent: CertDetailIntent)
}
