package com.tygern.caribou.restsupport

import org.eclipse.jetty.http.HttpMethod
import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.handler.AbstractHandler
import java.sql.SQLException
import javax.servlet.http.HttpServletResponse

abstract class BaseController : AbstractHandler() {
    fun post(uri: String, request: Request, httpServletResponse: HttpServletResponse, action: (List<String>) -> Unit) =
        handleRequest(
            request = request,
            uri = uri,
            response = httpServletResponse,
            action = action,
            method = HttpMethod.POST,
            statusCode = HttpServletResponse.SC_CREATED
        )

    fun get(uri: String, request: Request, httpServletResponse: HttpServletResponse, action: (List<String>) -> Unit) =
        handleRequest(
            request = request,
            uri = uri,
            response = httpServletResponse,
            action = action,
            method = HttpMethod.GET,
            statusCode = HttpServletResponse.SC_OK
        )

    fun delete(uri: String, request: Request, httpServletResponse: HttpServletResponse, action: (List<String>) -> Unit) =
        handleRequest(
            request = request,
            uri = uri,
            response = httpServletResponse,
            action = action,
            method = HttpMethod.DELETE,
            statusCode = HttpServletResponse.SC_NO_CONTENT
        )

    private fun handleRequest(
        request: Request,
        uri: String,
        response: HttpServletResponse,
        action: (List<String>) -> Unit,
        method: HttpMethod,
        statusCode: Int
    ) {
        if (request.method == method.toString()) {
            uri.toRegex().matchEntire(request.pathInfo)?.let {
                response.contentType = "application/json"
                try {
                    response.status = statusCode
                    action(it.groupValues.drop(1))
                } catch (e: SQLException) {
                    response.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
                }
                request.isHandled = true
            }
        }
    }
}