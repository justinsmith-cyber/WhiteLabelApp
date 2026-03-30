package com.velsol.feature.inventory

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value

interface InventoryComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed class Child {
        class ListChild(val component: InventoryListComponent) : Child()
        class DetailChild(val component: InventoryDetailComponent) : Child()
    }
}
