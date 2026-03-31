package com.velsol.feature.messages

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlin.time.Clock

interface MessagesRepository {
    fun getMessages(type: MessageType): Flow<List<Message>>
    suspend fun sendMessage(message: Message): Result<Unit>
    suspend fun markAsRead(messageId: String)
}

class MockMessagesRepository : MessagesRepository {

    private val _messages = MutableStateFlow(sampleMessages())

    override fun getMessages(type: MessageType): Flow<List<Message>> =
        _messages.map { list -> list.filter { it.type == type } }

    override suspend fun sendMessage(message: Message): Result<Unit> {
        delay(MOCK_DELAY_MS)
        _messages.update { it + message }
        return Result.success(Unit)
    }

    override suspend fun markAsRead(messageId: String) {
        _messages.update { list ->
            list.map { if (it.id == messageId) it.copy(isRead = true) else it }
        }
    }

    private companion object {
        const val MOCK_DELAY_MS = 800L

        fun sampleMessages(): List<Message> {
            val stop = MessageStop(id = "stop_1", name = "SGWS Las Vegas")
            val dispatcher = MessageUser(id = "dispatch_1", displayName = "Dispatch")
            val driver = MessageUser(id = "driver_1", displayName = "John Driver")
            val now = Clock.System.now().toEpochMilliseconds()
            return listOf(
                Message(
                    id = "msg_1",
                    subject = "Delivery Update",
                    body = "Your delivery is scheduled for tomorrow morning. " +
                        "Please ensure the loading dock is available.",
                    sender = dispatcher,
                    recipient = stop,
                    location = "Las Vegas, NV",
                    isHighPriority = false,
                    timestamp = now - 3_600_000L,
                    isRead = false,
                    type = MessageType.Incoming,
                ),
                Message(
                    id = "msg_2",
                    subject = "Route Change",
                    body = "Due to heavy traffic on I-15 we are taking an alternate route via Highway 95. ETA updated.",
                    sender = driver,
                    recipient = stop,
                    location = "Highway 95",
                    isHighPriority = true,
                    timestamp = now - 7_200_000L,
                    isRead = true,
                    type = MessageType.Incoming,
                ),
                Message(
                    id = "msg_3",
                    subject = "Invoice Discrepancy",
                    body = "Please review invoice #4521. There appears to be an error in the line items.",
                    sender = dispatcher,
                    recipient = stop,
                    location = "SGWS Las Vegas",
                    isHighPriority = true,
                    timestamp = now - 86_400_000L,
                    isRead = false,
                    type = MessageType.Incoming,
                ),
            )
        }
    }
}
