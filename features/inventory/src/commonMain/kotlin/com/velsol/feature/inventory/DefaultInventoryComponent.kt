package com.velsol.feature.inventory

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable

class DefaultInventoryComponent(
    componentContext: ComponentContext,
    // Repository is injected by the caller (DefaultRootComponent via AppGraph) rather than constructed here.
    repository: InventoryRepository,
) : InventoryComponent,
    ComponentContext by componentContext {

    private val getInventoryList = GetInventoryListUseCase(repository)
    private val getInventoryItem = GetInventoryItemUseCase(repository)

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, InventoryComponent.Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.List,
        handleBackButton = true,
        childFactory = ::createChild,
    )

    private fun createChild(
        config: Config,
        context: ComponentContext,
    ): InventoryComponent.Child = when (config) {
        Config.List -> InventoryComponent.Child.ListChild(
            DefaultInventoryListComponent(
                componentContext = context,
                getInventoryList = getInventoryList,
                onItemSelectedCallback = { sku -> navigation.push(Config.Detail(sku)) },
            ),
        )

        is Config.Detail -> InventoryComponent.Child.DetailChild(
            DefaultInventoryDetailComponent(
                componentContext = context,
                itemSku = config.sku,
                onBackCallback = { navigation.pop() },
                getInventoryItem = getInventoryItem,
            ),
        )
    }

    @Serializable
    sealed interface Config {
        @Serializable
        data object List : Config

        @Serializable
        data class Detail(val sku: String) : Config
    }
}
