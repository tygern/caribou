package com.tygern.caribou.lists

import com.fasterxml.jackson.databind.ObjectMapper
import com.tygern.caribou.restsupport.BaseController
import org.eclipse.jetty.server.Request
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class ListController(
    val mapper: ObjectMapper,
    val listRepository: ListRepository
) : BaseController() {
    override fun handle(s: String, request: Request, httpServletRequest: HttpServletRequest, httpServletResponse: HttpServletResponse) {
        post("/lists", request, httpServletResponse) {
            val list = mapper.readValue(request.reader, MessageList::class.java)

            listRepository.create(list).also {
                mapper.writeValue(httpServletResponse.outputStream, it)
            }
        }

        get("/lists/([^/]+)/?", request, httpServletResponse) { pathVariables ->
            val id = pathVariables.first()
            val list = listRepository.find(id)

            if (list == null) {
                httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND, "List not found")
            } else {
                mapper.writeValue(httpServletResponse.outputStream, list)
            }
        }

        post("/lists/([^/]+)/add/([^/]+)/?", request, httpServletResponse) { pathVariables ->
            val listId = pathVariables.first()
            val messageId = pathVariables[1]

            val newList = listRepository.addMessage(listId, messageId)

            if (newList == null) {
                httpServletResponse.status = HttpServletResponse.SC_BAD_REQUEST
            } else {
                mapper.writeValue(httpServletResponse.outputStream, newList)
            }

        }
    }
}
