package com.tygern.caribou.listserver


import com.tygern.caribou.lists.ListController
import com.tygern.caribou.lists.ListRepository
import com.tygern.caribou.lists.MessageClient
import com.tygern.caribou.restsupport.BaseApp
import com.tygern.caribou.restsupport.DefaultController
import com.tygern.caribou.restsupport.RestClient
import com.tygern.caribou.restsupport.objectMapper
import org.eclipse.jetty.server.handler.HandlerList
import java.util.TimeZone

class ListServer(port: Int) : BaseApp(port) {
    private val mapper = objectMapper()
    private val restClient = RestClient()
    private val messageServerUrl = System.getenv("MESSAGE_SERVER_URL")!!
    private val messageClient = MessageClient(messageServerUrl, restClient, mapper)
    private val listRepository = ListRepository(messageClient)

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
