package com.velsol.feature.messages

import kotlinx.coroutines.flow.StateFlow

data class NewMessageState(
    val to: MessageStop = MessageStop(id = "stop_1", name = "SGWS Las Vegas"),
    val from: MessageUser = MessageUser(id = "user_1", displayName = "Rohde, Brian"),
    val subject: String = "",
    val location: String = "",
    val isHighPriority: Boolean = false,
    val body: String = "",
    val isSending: Boolean = false,
    val sendSuccess: Boolean = false,
    val sendError: Boolean = false,
)

sealed interface NewMessageIntent {
    data class UpdateSubject(val text: String) : NewMessageIntent
    data class UpdateLocation(val text: String) : NewMessageIntent
    data class UpdateBody(val text: String) : NewMessageIntent
    data class ToggleHighPriority(val isChecked: Boolean) : NewMessageIntent
    data object ClickSend : NewMessageIntent
    data object ClickBack : NewMessageIntent
}

interface NewMessageComponent {
    val state: StateFlow<NewMessageState>

    fun onIntent(intent: NewMessageIntent)
}
