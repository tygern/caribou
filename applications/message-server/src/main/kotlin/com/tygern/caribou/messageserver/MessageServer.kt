package com.tygern.caribou.messageserver

import com.tygern.caribou.restsupport.BaseApp
import java.util.TimeZone

class MessageServer(port: Int) : BaseApp(port) {
    override fun controllers() = messageServerControllers
}

fun main(args: Array<String>) {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    MessageServer(8081).start()
}
