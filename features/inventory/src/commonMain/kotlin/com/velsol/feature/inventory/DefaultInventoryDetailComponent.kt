package com.velsol.feature.inventory

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DefaultInventoryDetailComponent internal constructor(
    componentContext: ComponentContext,
    override val itemSku: String,
    private val onBackCallback: () -> Unit,
    getInventoryItem: GetInventoryItemUseCase,
) : InventoryDetailComponent,
    ComponentContext by componentContext {

    private val _state = MutableStateFlow(InventoryDetailState(item = getInventoryItem(itemSku)))
    override val state: StateFlow<InventoryDetailState> = _state.asStateFlow()

    override fun onBack() {
        onBackCallback()
    }
}
