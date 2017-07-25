package com.tygern.caribou.restsupport

import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.client.utils.URIBuilder
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.BasicResponseHandler
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicNameValuePair

class RestClient {
    fun get(endpoint: String, vararg pairs: BasicNameValuePair) = execute(
        HttpGet(uriWithParams(endpoint, pairs))
    )

    fun post(endpoint: String, data: String) = execute(
        HttpPost(endpoint).apply {
            addHeader("Content-type", "application/json")
            entity = StringEntity(data)
        }
    )

    private fun execute(request: HttpUriRequest): String {
        return HttpClients.createDefault().execute(request, BasicResponseHandler())
    }

    private fun uriWithParams(endpoint: String, pairs: Array<out BasicNameValuePair>) =
        URIBuilder(endpoint).apply {
            pairs.forEach { pair -> addParameter(pair.name, pair.value) }
        }.build()
}