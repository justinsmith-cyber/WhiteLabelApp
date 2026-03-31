package com.velsol.feature.inventory

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DefaultInventoryListComponent internal constructor(
    componentContext: ComponentContext,
    getInventoryList: GetInventoryListUseCase,
    private val onItemSelectedCallback: (String) -> Unit,
) : InventoryListComponent,
    ComponentContext by componentContext {

    private val items = getInventoryList()

    private val _state = MutableStateFlow(
        InventoryListState(
            items = items,
            inStockCount = items.count { it.stockLevel == StockLevel.InStock },
            lowCount = items.count { it.stockLevel == StockLevel.LowStock },
        ),
    )
    override val state: StateFlow<InventoryListState> = _state.asStateFlow()

    override fun onIntent(intent: InventoryListIntent) {
        when (intent) {
            is InventoryListIntent.SelectItem -> onItemSelectedCallback(intent.sku)
        }
    }
}
