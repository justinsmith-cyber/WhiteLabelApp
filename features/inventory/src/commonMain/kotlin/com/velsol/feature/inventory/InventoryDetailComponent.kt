package com.velsol.feature.inventory

import kotlinx.coroutines.flow.StateFlow

data class InventoryDetailState(
    val item: InventoryItem? = null,
)

interface InventoryDetailComponent {
    val itemSku: String
    val state: StateFlow<InventoryDetailState>

    fun onBack()
}
