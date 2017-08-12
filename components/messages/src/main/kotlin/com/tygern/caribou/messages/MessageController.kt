package com.tygern.caribou.messages

import com.fasterxml.jackson.databind.ObjectMapper
import com.tygern.caribou.restsupport.BaseController
import org.eclipse.jetty.server.Request
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class MessageController(
    val mapper: ObjectMapper,
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

        get("/messages/([^/]+)/?", request, httpServletResponse) { pathVariables ->
            val id = pathVariables.first()
            val message = messageRepository.find(id)

            if (message == null) {
                httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND, "Message not found")
            } else {
                mapper.writeValue(httpServletResponse.outputStream, message)
            }
        }

        delete("/messages/([^/]+)/?", request, httpServletResponse) { pathVariables ->
            val id = pathVariables.first()

            messageRepository.delete(id)
        }

        put("/messages/([^/]+)/?", request, httpServletResponse) { pathVariables ->
            val id = pathVariables.first()
            val message = mapper.readValue(request.reader, Message::class.java)

            val updatedMessage = messageRepository.update(id, message)

            if (updatedMessage == null) {
                httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND, "Message not found")
            } else {
                mapper.writeValue(httpServletResponse.outputStream, updatedMessage)
            }
        }
    }
}
