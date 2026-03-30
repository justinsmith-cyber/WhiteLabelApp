package com.velsol.feature.inventory

import com.arkivanov.decompose.ComponentContext

class DefaultInventoryListComponent(
    componentContext: ComponentContext,
    private val onItemSelectedCallback: (String) -> Unit,
) : InventoryListComponent, ComponentContext by componentContext {

    override fun onItemSelected(sku: String) {
        onItemSelectedCallback(sku)
    }
}
