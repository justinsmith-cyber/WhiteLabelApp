package com.velsol.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.velsol.feature.certifications.CertificationsComponent
import com.velsol.feature.home.HomeComponent
import com.velsol.feature.inventory.InventoryComponent
import kotlinx.coroutines.flow.StateFlow

data class RootState(
    val selectedTab: RootComponent.Tab = RootComponent.Tab.Home,
    val showSwitcher: Boolean = false,
)

interface RootComponent {
    val stack: Value<ChildStack<*, Child>>
    val certifications: CertificationsComponent
    val inventory: InventoryComponent
    val state: StateFlow<RootState>

    fun onTabSelected(tab: Tab)
    fun onShowSwitcher()
    fun onHideSwitcher()

    enum class Tab { Home, Certifications, Inventory }

    sealed class Child {
        class HomeChild(val component: HomeComponent) : Child()
    }
}
