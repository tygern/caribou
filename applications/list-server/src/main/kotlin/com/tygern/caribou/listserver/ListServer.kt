package com.tygern.caribou.listserver


import com.tygern.caribou.restsupport.BaseApp
import java.util.TimeZone

class ListServer(port: Int) : BaseApp(port) {
    override fun controllers() = listServerControllers
}

fun main(args: Array<String>) {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    ListServer(8082).start()
}
