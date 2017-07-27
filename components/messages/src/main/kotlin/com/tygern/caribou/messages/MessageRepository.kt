package com.tygern.caribou.messages

import java.util.*


class MessageRepository {
    val messages = mutableMapOf<String, Message>()

    fun create(messageToCreate: Message) =
        messageToCreate
            .copy(id = generateId())
            .persist()


    fun find(id: String) = messages[id]

    fun list() = messages.values.toList()

    fun delete(id: String) {
        messages.remove(id)
    }

    fun update(id: String, updates: Message) =
        find(id)?.let {
            updates
                .copy(id = id)
                .persist()
        }

    private fun generateId() = UUID.randomUUID().toString()

    private fun Message.persist() = apply {
        messages[id!!] = this
    }
}