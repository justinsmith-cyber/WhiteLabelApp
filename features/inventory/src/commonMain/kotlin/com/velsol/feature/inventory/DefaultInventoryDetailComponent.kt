package com.velsol.feature.inventory

import com.arkivanov.decompose.ComponentContext

class DefaultInventoryDetailComponent(
    componentContext: ComponentContext,
    override val itemSku: String,
    private val onBackCallback: () -> Unit,
) : InventoryDetailComponent,
    ComponentContext by componentContext {

    override fun onBack() {
        onBackCallback()
    }
}
