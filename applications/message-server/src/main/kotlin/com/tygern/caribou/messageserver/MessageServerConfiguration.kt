package com.tygern.caribou.messageserver

import com.tygern.caribou.messages.MessageController
import com.tygern.caribou.messages.MessageRepository
import com.tygern.caribou.restsupport.DefaultController
import com.tygern.caribou.restsupport.objectMapper

private val mapper = objectMapper()

val messageServerControllers = listOf(
    MessageController(mapper, MessageRepository()),
    DefaultController()
)
