package com.tygern.caribou.lists


data class Message(
    val id: String,
    val value: String,
    val deleted: Boolean = false
)