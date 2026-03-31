package com.velsol.feature.login

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable

class DefaultLoginComponent(
    componentContext: ComponentContext,
    // Repositories are injected by DefaultRootComponent rather than constructed here.
    private val authRepository: AuthRepository,
    private val supportRepository: SupportRepository,
    private val onLoginSuccessCallback: () -> Unit,
) : LoginComponent,
    ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, LoginComponent.Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.LoginScreen,
        handleBackButton = true,
        childFactory = ::createChild,
    )

    private fun createChild(config: Config, context: ComponentContext): LoginComponent.Child =
        when (config) {
            Config.LoginScreen -> LoginComponent.Child.LoginScreenChild(
                DefaultLoginScreenComponent(
                    componentContext = context,
                    authRepository = authRepository,
                    onLoginSuccessCallback = onLoginSuccessCallback,
                    onNavigateToSupportCallback = { navigation.push(Config.Support) },
                ),
            )

            Config.Support -> LoginComponent.Child.SupportChild(
                DefaultSupportComponent(
                    componentContext = context,
                    supportRepository = supportRepository,
                    onBackCallback = { navigation.pop() },
                ),
            )
        }

    @Serializable
    sealed interface Config {
        @Serializable
        data object LoginScreen : Config

        @Serializable
        data object Support : Config
    }
}
