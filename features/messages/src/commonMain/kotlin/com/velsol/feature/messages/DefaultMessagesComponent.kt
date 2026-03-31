package com.velsol.feature.messages

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable

class DefaultMessagesComponent(
    componentContext: ComponentContext,
    private val repository: MessagesRepository,
) : MessagesComponent,
    ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, MessagesComponent.Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.List,
        handleBackButton = true,
        childFactory = ::createChild,
    )

    @OptIn(DelicateDecomposeApi::class)
    private fun createChild(config: Config, context: ComponentContext): MessagesComponent.Child =
        when (config) {
            Config.List -> MessagesComponent.Child.ListChild(
                DefaultMessagesListComponent(
                    componentContext = context,
                    repository = repository,
                    onNavigateToNewMessage = { navigation.push(Config.NewMessage) },
                ),
            )

            Config.NewMessage -> MessagesComponent.Child.NewMessageChild(
                DefaultNewMessageComponent(
                    componentContext = context,
                    repository = repository,
                    currentStop = MessageStop(id = "stop_1", name = "SGWS Las Vegas"),
                    currentUser = MessageUser(id = "user_1", displayName = "Rohde, Brian"),
                    onBackCallback = { navigation.pop() },
                ),
            )
        }

    @Serializable
    sealed interface Config {
        @Serializable
        data object List : Config

        @Serializable
        data object NewMessage : Config
    }
}
