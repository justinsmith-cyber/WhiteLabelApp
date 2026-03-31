package com.velsol.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.velsol.di.appGraph
import com.velsol.feature.certifications.CertificationsComponent
import com.velsol.feature.certifications.DefaultCertificationsComponent
import com.velsol.feature.home.DefaultHomeComponent
import com.velsol.feature.inventory.DefaultInventoryComponent
import com.velsol.feature.inventory.InventoryComponent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.Serializable

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
    }

    private val navigation = StackNavigation<Config>()

    private val _state = MutableStateFlow(RootState())
    override val state: StateFlow<RootState> = _state.asStateFlow()

    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.Home,
        handleBackButton = true,
        childFactory = ::createChild,
    )

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

    override fun onIntent(intent: RootIntent) {
        when (intent) {
            is RootIntent.SelectTab -> _state.update { it.copy(selectedTab = intent.tab) }
        }
    }

    private fun createChild(config: Config, context: ComponentContext): RootComponent.Child = when (config) {
        Config.Home -> RootComponent.Child.HomeChild(
            DefaultHomeComponent(
                componentContext = context,
            ),
        )
    }

    @Serializable
    sealed interface Config {
        @Serializable
        data object Home : Config
    }
}
