package com.tygern.caribou.restsupport

import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.client.ResponseHandler
import org.apache.http.util.EntityUtils


class HttpResponseHandler : ResponseHandler<Response> {
    override fun handleResponse(response: HttpResponse): Response {
        val statusCode = response.statusLine.statusCode
        val entity: HttpEntity? = response.entity

        return if (statusCode >= 300) {
            EntityUtils.consume(entity)

            Response.Error(statusCode, response.statusLine.reasonPhrase)
        } else {
            val body: String = entity?.let { EntityUtils.toString(it) } ?: ""

            Response.Success(statusCode, body)
        }
    }
}

sealed class Response {
    data class Success(val statusCode: Int, val body: String) : Response()
    data class Error(val statusCode: Int, val message: String) : Response()
}
