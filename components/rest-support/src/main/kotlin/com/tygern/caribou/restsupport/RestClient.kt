package com.tygern.caribou.restsupport

import org.apache.http.client.methods.*
import org.apache.http.client.utils.URIBuilder
import org.apache.http.entity.StringEntity
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

    fun put(endpoint: String, data: String) = execute(
        HttpPut(endpoint).apply {
            addHeader("Content-type", "application/json")
            entity = StringEntity(data)
        }
    )

    fun delete(endpoint: String, vararg pairs: BasicNameValuePair) = execute(
        HttpDelete(uriWithParams(endpoint, pairs))
    )

    private fun execute(request: HttpUriRequest): Response {
        return HttpClients.createDefault().execute(request, HttpResponseHandler())
    }

    private fun uriWithParams(endpoint: String, pairs: Array<out BasicNameValuePair>) =
        URIBuilder(endpoint).apply {
            pairs.forEach { pair -> addParameter(pair.name, pair.value) }
        }.build()
}