package com.tygern.caribou.restsupport

import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.handler.AbstractHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class DefaultController : AbstractHandler() {
    override fun handle(s: String, request: Request, httpServletRequest: HttpServletRequest, httpServletResponse: HttpServletResponse) {
        httpServletResponse.contentType = "text/html; charset=UTF-8"
        httpServletResponse.outputStream.write("Caribou".toByteArray())
        httpServletResponse.status = HttpServletResponse.SC_OK
        request.isHandled = true
    }
}
