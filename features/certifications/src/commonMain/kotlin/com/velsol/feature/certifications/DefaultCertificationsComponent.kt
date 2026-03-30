package com.velsol.feature.certifications

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import io.ktor.client.HttpClient
import kotlinx.serialization.Serializable

class DefaultCertificationsComponent(
    componentContext: ComponentContext,
    httpClient: HttpClient,
    apiBaseUrl: String,
) : CertificationsComponent,
    ComponentContext by componentContext {

    // Single repository instance shared across all child components in this sub-graph.
    private val repository: CertificationsRepository = DefaultCertificationsRepository(httpClient, apiBaseUrl)
    private val getCertifications = GetCertificationsUseCase(repository)
    private val getCertification = GetCertificationUseCase(repository)

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, CertificationsComponent.Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.List,
        handleBackButton = true,
        childFactory = ::createChild,
    )

    private fun createChild(
        config: Config,
        context: ComponentContext,
    ): CertificationsComponent.Child = when (config) {
        Config.List -> CertificationsComponent.Child.ListChild(
            DefaultCertListComponent(
                componentContext = context,
                getCertifications = getCertifications,
                onCertSelectedCallback = { name -> navigation.push(Config.Detail(name)) },
            ),
        )

        is Config.Detail -> CertificationsComponent.Child.DetailChild(
            DefaultCertDetailComponent(
                componentContext = context,
                certName = config.certName,
                onBackCallback = { navigation.pop() },
                getCertification = getCertification,
            ),
        )
    }

    @Serializable
    sealed interface Config {
        @Serializable
        data object List : Config

        @Serializable
        data class Detail(val certName: String) : Config
    }
}
