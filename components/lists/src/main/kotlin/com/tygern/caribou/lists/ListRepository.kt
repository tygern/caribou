package com.tygern.caribou.lists

import java.util.UUID


class ListRepository {
    private val messageLists = mutableMapOf<String, MessageListRecord>()

    fun save(record: MessageListRecord) = record.persist()

    fun find(id: String) = messageLists[id]

    private fun MessageListRecord.persist() =
        copy(id = generateIdIfMissing()).also {
            messageLists[it.id!!] = it
        }

    private fun MessageListRecord.generateIdIfMissing() =
        id ?: UUID.randomUUID().toString()
}
