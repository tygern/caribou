package com.tygern.caribou.lists


data class MessageListRecord(
    val id: String? = null,
    val title: String,
    val messageIds: List<String>
)