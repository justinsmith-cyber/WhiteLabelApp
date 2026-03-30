package com.velsol.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.velsol.core.network.createHttpClient
import com.velsol.di.appBrandConfig
import com.velsol.feature.certifications.CertificationsComponent
import com.velsol.feature.certifications.DefaultCertificationsComponent
import com.velsol.feature.home.DefaultHomeComponent
import com.velsol.feature.inventory.DefaultInventoryComponent
import com.velsol.feature.inventory.InventoryComponent
import kotlinx.serialization.Serializable

class DefaultRootComponent(
    componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext {

    private val brandConfig = appBrandConfig()
    private val httpClient = createHttpClient()

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.Home,
        handleBackButton = true,
        childFactory = ::createChild
    )

    override val certifications: CertificationsComponent =
        DefaultCertificationsComponent(
            componentContext = childContext("certifications"),
            httpClient = httpClient,
            apiBaseUrl = brandConfig.apiBaseUrl,
        )

    override val inventory: InventoryComponent =
        DefaultInventoryComponent(childContext("inventory"))

    private fun createChild(config: Config, context: ComponentContext): RootComponent.Child =
        when (config) {
            Config.Home -> RootComponent.Child.HomeChild(DefaultHomeComponent(context))
        }

    @Serializable
    sealed interface Config {
        @Serializable
        data object Home : Config
    }
}
