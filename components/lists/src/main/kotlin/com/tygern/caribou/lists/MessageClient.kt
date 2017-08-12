package com.tygern.caribou.lists

import com.fasterxml.jackson.databind.ObjectMapper
import com.tygern.caribou.restsupport.Response
import com.tygern.caribou.restsupport.RestClient

open class MessageClient(
    private val messageServerUrl: String,
    private val restClient: RestClient,
    private val mapper: ObjectMapper
) {
    open fun find(id: String): Message? {
        val response = restClient.get("$messageServerUrl/messages/$id")

        return when(response) {
            is Response.Success -> mapper.readValue(response.body, Message::class.java)
            is Response.Error -> null
        }
    }
}