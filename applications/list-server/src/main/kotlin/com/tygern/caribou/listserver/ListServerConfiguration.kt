package com.tygern.caribou.listserver

import com.tygern.caribou.lists.ListController
import com.tygern.caribou.lists.ListRepository
import com.tygern.caribou.lists.ListService
import com.tygern.caribou.lists.MessageClient
import com.tygern.caribou.restsupport.DefaultController
import com.tygern.caribou.restsupport.RestClient
import com.tygern.caribou.restsupport.objectMapper


private val mapper = objectMapper()
private val restClient = RestClient()
private val messageServerUrl = System.getenv("MESSAGE_SERVER_URL")!!
private val messageClient = MessageClient(messageServerUrl, restClient, mapper)
private val listRepository = ListRepository()
private val listService = ListService(messageClient, listRepository)

val listServerControllers = listOf(
    ListController(mapper, listService),
    DefaultController()
)
