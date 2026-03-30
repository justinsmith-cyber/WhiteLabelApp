package com.velsol.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.velsol.feature.certifications.CertificationsComponent
import com.velsol.feature.home.HomeComponent
import com.velsol.feature.inventory.InventoryComponent

interface RootComponent {
    val stack: Value<ChildStack<*, Child>>
    val certifications: CertificationsComponent
    val inventory: InventoryComponent

    sealed class Child {
        class HomeChild(val component: HomeComponent) : Child()
    }
}
