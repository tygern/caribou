package com.tygern.caribou.lists

class ListService(
    private val messageClient: MessageClient,
    private val repository: ListRepository
) {
    fun create(listToCreate: MessageList) = listToCreate.persist()

    fun addMessage(id: String, messageId: String) =
        find(id)?.let { existingRecord ->
            val messageToAdd = messageClient
                .find(messageId)
                ?.takeIf { !it.deleted }
                ?: return null

            existingRecord
                .copy(messages = existingRecord.messages.plus(messageToAdd))
                .persist()
        }

    fun find(id: String) = repository.find(id)?.toMessageList()

    private fun MessageList.persist() =
        repository.save(this.toRecord()).toMessageList()

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
