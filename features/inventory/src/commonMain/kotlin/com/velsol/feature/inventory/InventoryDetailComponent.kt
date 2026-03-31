package com.velsol.feature.inventory

import kotlinx.coroutines.flow.StateFlow

data class InventoryDetailState(
    val item: InventoryItem? = null,
)

sealed interface InventoryDetailIntent {
    data object Back : InventoryDetailIntent
}

interface InventoryDetailComponent {
    val itemSku: String
    val state: StateFlow<InventoryDetailState>

    fun onIntent(intent: InventoryDetailIntent)
}
