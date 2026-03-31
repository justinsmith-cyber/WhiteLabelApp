package com.velsol.feature.home

import kotlinx.coroutines.flow.StateFlow

data class HomeState(
    val tapCount: Int = 0,
)

sealed interface HomeIntent {
    data object LogoTapped : HomeIntent
}

interface HomeComponent {
    val state: StateFlow<HomeState>

    fun onIntent(intent: HomeIntent)
}
