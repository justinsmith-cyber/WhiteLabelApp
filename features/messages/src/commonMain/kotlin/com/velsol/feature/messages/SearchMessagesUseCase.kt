package com.velsol.feature.messages

internal class SearchMessagesUseCase {
    operator fun invoke(messages: List<Message>, query: String): List<Message> {
        if (query.isBlank()) return messages
        return messages.filter { msg ->
            msg.subject.contains(query, ignoreCase = true) ||
                msg.sender.displayName.contains(query, ignoreCase = true) ||
                msg.recipient.name.contains(query, ignoreCase = true)
        }
    }
}
