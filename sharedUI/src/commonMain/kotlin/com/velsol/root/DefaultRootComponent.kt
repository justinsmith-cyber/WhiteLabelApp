package com.velsol.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.velsol.di.appGraph
import com.velsol.feature.certifications.CertificationsComponent
import com.velsol.feature.certifications.DefaultCertificationsComponent
import com.velsol.feature.home.DefaultHomeComponent
import com.velsol.feature.home.HomeComponent
import com.velsol.feature.inventory.DefaultInventoryComponent
import com.velsol.feature.inventory.InventoryComponent
import com.velsol.feature.login.DefaultLoginComponent
import com.velsol.feature.login.LoginComponent
import com.velsol.feature.login.MockSupportRepository
import com.velsol.feature.messages.DefaultMessagesComponent
import com.velsol.feature.messages.MessagesComponent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DefaultRootComponent(
    componentContext: ComponentContext,
) : RootComponent,
    ComponentContext by componentContext {

    private val graph = appGraph()
    private val brandConfig = graph.brandConfig

    override val visibleTabs: List<RootComponent.Tab> = buildList {
        add(RootComponent.Tab.Home)
        if (brandConfig.features.hasHvacCertifications) add(RootComponent.Tab.Certifications)
        if (brandConfig.features.hasPlumbingInventory) add(RootComponent.Tab.Inventory)
        add(RootComponent.Tab.Messages)
    }

    private val _state = MutableStateFlow(RootState())
    override val state: StateFlow<RootState> = _state.asStateFlow()

    override val login: LoginComponent by lazy {
        DefaultLoginComponent(
            componentContext = childContext("login"),
            authRepository = graph.authRepository,
            supportRepository = MockSupportRepository(brandConfig),
            onLoginSuccessCallback = { onIntent(RootIntent.LoginSuccess) },
        )
    }

    // Structural intent: expose all root tabs uniformly as lazy feature components.
    override val home: HomeComponent by lazy {
        DefaultHomeComponent(componentContext = childContext("home"))
    }

    // Lazily create feature components so their child stacks/data loading do not run at app startup.
    override val certifications: CertificationsComponent by lazy {
        DefaultCertificationsComponent(
            componentContext = childContext("certifications"),
            repository = graph.certificationsRepository,
        )
    }

    // Defers inventory setup/allocation until the user visits that tab.
    override val inventory: InventoryComponent by lazy {
        DefaultInventoryComponent(
            componentContext = childContext("inventory"),
            repository = graph.inventoryRepository,
        )
    }

    override val messages: MessagesComponent by lazy {
        DefaultMessagesComponent(
            componentContext = childContext("messages"),
            repository = graph.messagesRepository,
        )
    }

    override fun onIntent(intent: RootIntent) {
        when (intent) {
            is RootIntent.SelectTab -> _state.update { it.copy(selectedTab = intent.tab) }
            RootIntent.LoginSuccess -> _state.update { it.copy(isLoggedIn = true) }
        }
    }
}
