package com.velsol.feature.inventory

import kotlinx.coroutines.flow.StateFlow

data class InventoryListState(
    val items: List<InventoryItem> = emptyList(),
    val inStockCount: Int = 0,
    val lowCount: Int = 0,
)

interface InventoryListComponent {
    val state: StateFlow<InventoryListState>

    fun onItemSelected(sku: String)
}
