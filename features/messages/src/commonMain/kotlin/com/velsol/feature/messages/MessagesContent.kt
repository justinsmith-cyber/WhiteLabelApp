package com.velsol.feature.messages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState

@Composable
fun MessagesContent(
    component: MessagesComponent,
    modifier: Modifier = Modifier,
) {
    val childStack by component.stack.subscribeAsState()

    Children(
        stack = childStack,
        modifier = modifier,
        animation = stackAnimation(fade()),
    ) { child ->
        when (val instance = child.instance) {
            is MessagesComponent.Child.ListChild ->
                MessagesListContent(component = instance.component)

            is MessagesComponent.Child.NewMessageChild ->
                NewMessageContent(component = instance.component)
        }
    }
}
