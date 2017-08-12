package test.tygern.caribou.listserver

import com.fasterxml.jackson.databind.ObjectMapper
import com.tygern.caribou.lists.Message
import com.tygern.caribou.restsupport.BaseApp
import com.tygern.caribou.restsupport.BaseController
import com.tygern.caribou.restsupport.objectMapper
import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.handler.HandlerList
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class FakeMessageServer(port: Int) : BaseApp(port) {
    private val mapper = objectMapper()

    init {
        server.handler = HandlerList().apply {
            addHandler(DummyController(mapper))
        }
    }
}


class DummyController(private val mapper: ObjectMapper) : BaseController() {

    override fun handle(s: String, request: Request, httpServletRequest: HttpServletRequest, httpServletResponse: HttpServletResponse) {
        get("/messages/i-exist", request, httpServletResponse) {
            httpServletResponse.contentType = "application/json"
            mapper.writeValue(httpServletResponse.outputStream, Message("i-exist", "I exist"))
        }

        get("*", request, httpServletResponse) {
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND, "Message not found")
        }
    }
}