package com.velsol.feature.messages

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value

interface MessagesComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed class Child {
        class ListChild(val component: MessagesListComponent) : Child()
        class NewMessageChild(val component: NewMessageComponent) : Child()
    }
}
