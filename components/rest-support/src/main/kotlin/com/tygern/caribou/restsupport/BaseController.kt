package com.tygern.caribou.restsupport

import org.eclipse.jetty.http.HttpMethod
import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.handler.AbstractHandler
import java.sql.SQLException
import javax.servlet.http.HttpServletResponse

abstract class BaseController : AbstractHandler() {
    fun post(uri: String, request: Request, httpServletResponse: HttpServletResponse, block: (List<String>) -> Unit) {
        if (request.method == HttpMethod.POST.toString()) {
            uri.toRegex().matchEntire(request.pathInfo)?.let {
                httpServletResponse.contentType = "application/json"
                try {
                    httpServletResponse.status = HttpServletResponse.SC_CREATED
                    block(it.groupValues.drop(1))
                } catch (e: SQLException) {
                    httpServletResponse.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
                }
                request.isHandled = true
            }
        }
    }

    fun get(uri: String, request: Request, httpServletResponse: HttpServletResponse, block: (List<String>) -> Unit) {
        if (request.method == HttpMethod.GET.toString()) {
            uri.toRegex().matchEntire(request.pathInfo)?.let {
                httpServletResponse.contentType = "application/json"
                try {
                    httpServletResponse.status = HttpServletResponse.SC_OK
                    block(it.groupValues.drop(1))
                } catch (e: SQLException) {
                    httpServletResponse.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
                }
                request.isHandled = true
            }
        }
    }
}