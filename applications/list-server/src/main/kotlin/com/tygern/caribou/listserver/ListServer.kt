package com.tygern.caribou.listserver


import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.tygern.caribou.lists.ListController
import com.tygern.caribou.lists.ListRepository
import com.tygern.caribou.lists.MessageClient
import com.tygern.caribou.restsupport.BaseApp
import com.tygern.caribou.restsupport.DefaultController
import com.tygern.caribou.restsupport.RestClient
import org.eclipse.jetty.server.handler.HandlerList
import java.util.TimeZone

class ListServer(port: Int) : BaseApp(port) {
    val mapper: ObjectMapper = ObjectMapper().registerKotlinModule().registerModule(JavaTimeModule())
    val restClient = RestClient()
    val messageServerUrl = System.getenv("MESSAGE_SERVER_URL")!!
    val messageClient = MessageClient(messageServerUrl, restClient, mapper)
    val listRepository = ListRepository(messageClient)

    init {
        server.handler = HandlerList().apply {
            addHandler(ListController(mapper, listRepository))
            addHandler(DefaultController())
        }
    }
}

fun main(args: Array<String>) {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    ListServer(8082).start()
}
