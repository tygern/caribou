package test.tygern.caribou.listserver

import com.tygern.caribou.lists.Message
import com.tygern.caribou.restsupport.BaseApp
import com.tygern.caribou.restsupport.BaseController
import com.tygern.caribou.restsupport.objectMapper
import org.eclipse.jetty.server.Request
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

private val mapper = objectMapper()

class FakeMessageServer(port: Int) : BaseApp(port) {
    override fun controllers() = listOf(DummyController())
}

class DummyController : BaseController() {

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