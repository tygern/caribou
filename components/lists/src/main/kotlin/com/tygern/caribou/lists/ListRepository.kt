package com.tygern.caribou.lists

import java.util.UUID

class ListRepository(private val messageClient: MessageClient) {
    val messageLists = mutableMapOf<String, MessageListRecord>()

    fun create(listToCreate: MessageList) =
        listToCreate
            .copy(id = generateId())
            .persist()

    fun addMessage(id: String, messageId: String): MessageList? {
        val existingRecord = find(id) ?: return null
        val messageToAdd = messageClient.find(messageId) ?: return null

        return MessageList(
            id = existingRecord.id,
            title = existingRecord.title,
            messages = existingRecord.messages.plus(messageToAdd)
        ).persist()
    }

    fun find(id: String) = messageLists[id]?.toMessageList()

    private fun MessageList.persist() = apply {
        messageLists[id!!] = this.toRecord()
    }

    private fun MessageList.toRecord() = MessageListRecord(
        id = id,
        title = title,
        messageIds = messages.map { it.id }
    )

    private fun MessageListRecord.toMessageList() = MessageList(
        id = id,
        title = title,
        messages = messageIds.map { messageClient.find(it)!! }
    )

    private fun generateId() = UUID.randomUUID().toString()


}

class MessageNotFoundException : Throwable()
class ListNotFoundException : Throwable()
