package com.tygern.caribou.messageserver

import com.tygern.caribou.messages.MessageController
import com.tygern.caribou.messages.MessageRepository
import com.tygern.caribou.restsupport.BaseApp
import com.tygern.caribou.restsupport.DefaultController
import com.tygern.caribou.restsupport.objectMapper
import org.eclipse.jetty.server.handler.HandlerList
import java.util.TimeZone

class MessageServer(port: Int) : BaseApp(port) {
    private val mapper = objectMapper()

    init {
        server.handler = HandlerList().apply {
            addHandler(MessageController(mapper, MessageRepository()))
            addHandler(DefaultController())
        }
    }
}

fun main(args: Array<String>) {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    MessageServer(8081).start()
}
