package com.tygern.caribou.messages

import java.util.*


class MessageRepository {
    private val messages = mutableMapOf<String, Message>()

    fun create(messageToCreate: Message) =
        messageToCreate
            .copy(id = generateId())
            .persist()


    fun find(id: String) = messages[id]

    fun list() = messages.values
        .filter { !it.deleted }
        .toList()

    fun delete(id: String) {
        find(id)?.let {
            update(id, it.copy(deleted = true))
        }
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