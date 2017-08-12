package com.tygern.caribou.lists


data class MessageList(
    val id: String? = null,
    val title: String,
    val messages: List<Message> = emptyList()
)