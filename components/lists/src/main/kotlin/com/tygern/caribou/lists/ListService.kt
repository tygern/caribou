package com.tygern.caribou.lists

class ListService(
    private val messageClient: MessageClient,
    private val repository: ListRepository
) {
    fun create(listToCreate: MessageList) =
        listToCreate
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

    fun find(id: String) = repository.find(id)?.toMessageList()

    private fun MessageList.persist() = let {
        repository.save(it.toRecord()).toMessageList()
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
}
