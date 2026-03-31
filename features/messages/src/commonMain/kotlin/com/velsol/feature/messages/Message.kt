package com.velsol.feature.messages

data class MessageStop(
    val id: String,
    val name: String,
)

data class MessageUser(
    val id: String,
    val displayName: String,
)

enum class MessageType { Incoming, Outgoing }

data class Message(
    val id: String,
    val subject: String,
    val body: String,
    val sender: MessageUser,
    val recipient: MessageStop,
    val location: String,
    val isHighPriority: Boolean,
    val timestamp: Long,
    val isRead: Boolean,
    val type: MessageType,
)
