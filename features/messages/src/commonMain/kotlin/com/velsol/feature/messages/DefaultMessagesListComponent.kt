package com.velsol.feature.messages

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class DefaultMessagesListComponent(
    componentContext: ComponentContext,
    private val repository: MessagesRepository,
    private val onNavigateToNewMessage: () -> Unit,
) : MessagesListComponent,
    ComponentContext by componentContext {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _selectedTab = MutableStateFlow(MessagesTab.Incoming)
    private val _searchQuery = MutableStateFlow("")

    private val _state = MutableStateFlow(
        MessagesListState(
            isLoading = true,
            headerDate = buildHeaderDate(),
        ),
    )
    override val state: StateFlow<MessagesListState> = _state.asStateFlow()

    init {
        lifecycle.doOnDestroy { scope.cancel() }
        observeMessages()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeMessages() {
        scope.launch {
            _selectedTab
                .flatMapLatest { tab -> repository.getMessages(tab.toMessageType()) }
                .combine(_searchQuery) { messages, query -> messages to query }
                .collect { (messages, query) ->
                    val filtered = if (query.isBlank()) {
                        messages
                    } else {
                        messages.filter { msg ->
                            msg.subject.contains(query, ignoreCase = true) ||
                                msg.sender.displayName.contains(query, ignoreCase = true) ||
                                msg.recipient.name.contains(query, ignoreCase = true)
                        }
                    }
                    _state.update {
                        it.copy(
                            selectedTab = _selectedTab.value,
                            searchQuery = query,
                            messages = filtered,
                            isLoading = false,
                            totalCount = messages.size,
                            unreadCount = messages.count { msg -> !msg.isRead },
                        )
                    }
                }
        }
    }

    override fun onIntent(intent: MessagesListIntent) {
        when (intent) {
            is MessagesListIntent.SelectTab -> {
                _state.update { it.copy(isLoading = true, selectedTab = intent.tab) }
                _selectedTab.update { intent.tab }
            }

            is MessagesListIntent.UpdateSearchQuery -> _searchQuery.update { intent.query }

            MessagesListIntent.ClickNewMessage -> onNavigateToNewMessage()

            is MessagesListIntent.ClickMessage -> scope.launch {
                repository.markAsRead(intent.id)
            }
        }
    }

    private fun buildHeaderDate(): String {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val dayOfWeek = now.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }
        val month = now.month.name.lowercase().replaceFirstChar { it.uppercase() }
        val monthAbbrev = month.take(MonthAbbrevLength)
        return "$dayOfWeek $monthAbbrev ${now.day}, ${now.year}"
    }
}

private const val MonthAbbrevLength = 3

private fun MessagesTab.toMessageType(): MessageType = when (this) {
    MessagesTab.Incoming -> MessageType.Incoming
    MessagesTab.Outgoing -> MessageType.Outgoing
}
