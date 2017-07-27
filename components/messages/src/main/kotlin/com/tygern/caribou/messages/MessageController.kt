package com.tygern.caribou.messages

import com.fasterxml.jackson.databind.ObjectMapper
import com.tygern.caribou.restsupport.BaseController
import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.handler.AbstractHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class MessageController(
    val mapper : ObjectMapper,
    val messageRepository: MessageRepository
) : BaseController() {
    override fun handle(s: String, request: Request, httpServletRequest: HttpServletRequest, httpServletResponse: HttpServletResponse) {
        post("/messages", request, httpServletResponse) {
            val message = mapper.readValue(request.reader, Message::class.java)

            messageRepository.create(message).also {
                mapper.writeValue(httpServletResponse.outputStream, it)
            }
        }

        get("/messages", request, httpServletResponse) {
            val list = messageRepository.list()

            mapper.writeValue(httpServletResponse.outputStream, list)
        }
    }
}
