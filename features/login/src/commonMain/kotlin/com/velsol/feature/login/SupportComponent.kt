package com.velsol.feature.login

import kotlinx.coroutines.flow.StateFlow

sealed interface SupportState {
    data object Loading : SupportState
    data class Content(val data: SupportData) : SupportState
}

interface SupportComponent {
    val state: StateFlow<SupportState>

    fun onBack()
}
