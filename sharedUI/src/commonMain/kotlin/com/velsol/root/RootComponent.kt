package com.velsol.root

import com.velsol.feature.certifications.CertificationsComponent
import com.velsol.feature.home.HomeComponent
import com.velsol.feature.inventory.InventoryComponent
import com.velsol.feature.login.LoginComponent
import com.velsol.feature.messages.MessagesComponent
import kotlinx.coroutines.flow.StateFlow

data class RootState(
    val selectedTab: RootComponent.Tab = RootComponent.Tab.Home,
    val isLoggedIn: Boolean = false,
)

sealed interface RootIntent {
    data class SelectTab(val tab: RootComponent.Tab) : RootIntent
    data object LoginSuccess : RootIntent
}

interface RootComponent {
    val login: LoginComponent
    val home: HomeComponent
    val certifications: CertificationsComponent
    val inventory: InventoryComponent
    val messages: MessagesComponent
    val state: StateFlow<RootState>
    /** Ordered list of tabs enabled for the active brand; drives navigation bar visibility. */
    val visibleTabs: List<Tab>

    fun onIntent(intent: RootIntent)

    enum class Tab { Home, Certifications, Inventory, Messages }
}
