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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Clock

class DefaultNewMessageComponent(
    componentContext: ComponentContext,
    private val repository: MessagesRepository,
    currentStop: MessageStop,
    currentUser: MessageUser,
    private val onBackCallback: () -> Unit,
) : NewMessageComponent,
    ComponentContext by componentContext {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _state = MutableStateFlow(
        NewMessageState(
            to = currentStop,
            from = currentUser,
        ),
    )
    override val state: StateFlow<NewMessageState> = _state.asStateFlow()

    init {
        lifecycle.doOnDestroy { scope.cancel() }
    }

    override fun onIntent(intent: NewMessageIntent) {
        when (intent) {
            is NewMessageIntent.UpdateSubject ->
                _state.update { it.copy(subject = intent.text, sendError = false) }

            is NewMessageIntent.UpdateLocation ->
                _state.update { it.copy(location = intent.text) }

            is NewMessageIntent.UpdateBody ->
                _state.update { it.copy(body = intent.text) }

            is NewMessageIntent.ToggleHighPriority ->
                _state.update { it.copy(isHighPriority = intent.isChecked) }

            NewMessageIntent.ClickSend -> sendMessage()

            NewMessageIntent.ClickBack -> onBackCallback()
        }
    }

    private fun sendMessage() {
        if (_state.value.isSending) return
        val current = _state.value
        if (current.subject.isBlank()) {
            _state.update { it.copy(sendError = true) }
            return
        }
        scope.launch {
            _state.update { it.copy(isSending = true, sendError = false) }
            val outgoing = Message(
                id = "msg_${Clock.System.now().toEpochMilliseconds()}",
                subject = current.subject,
                body = current.body,
                sender = current.from,
                recipient = current.to,
                location = current.location,
                isHighPriority = current.isHighPriority,
                timestamp = Clock.System.now().toEpochMilliseconds(),
                isRead = true,
                type = MessageType.Outgoing,
            )
            repository.sendMessage(outgoing)
                .onSuccess { onBackCallback() }
                .onFailure { _state.update { s -> s.copy(isSending = false, sendError = true) } }
        }
    }
}
