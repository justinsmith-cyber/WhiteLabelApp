package com.velsol.feature.messages

import kotlinx.coroutines.flow.StateFlow

enum class MessagesTab { Incoming, Outgoing }

data class MessagesListState(
    val selectedTab: MessagesTab = MessagesTab.Incoming,
    val searchQuery: String = "",
    val messages: List<Message> = emptyList(),
    val isLoading: Boolean = true,
    val totalCount: Int = 0,
    val unreadCount: Int = 0,
    val headerDate: String = "",
)

sealed interface MessagesListIntent {
    data class SelectTab(val tab: MessagesTab) : MessagesListIntent
    data class UpdateSearchQuery(val query: String) : MessagesListIntent
    data object ClickNewMessage : MessagesListIntent
    data class ClickMessage(val id: String) : MessagesListIntent
}

interface MessagesListComponent {
    val state: StateFlow<MessagesListState>

    fun onIntent(intent: MessagesListIntent)
}
