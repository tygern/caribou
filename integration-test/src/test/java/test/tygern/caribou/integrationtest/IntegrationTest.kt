package test.tygern.caribou.integrationtest

import com.tygern.caribou.lists.MessageList
import com.tygern.caribou.listserver.ListServer
import com.tygern.caribou.messages.Message
import com.tygern.caribou.messageserver.MessageServer
import com.tygern.caribou.restsupport.Response
import com.tygern.caribou.restsupport.RestClient
import com.tygern.caribou.restsupport.objectMapper
import io.damo.aspen.Test
import io.tygern.caribou.testsupport.assertSuccess
import org.assertj.core.api.Assertions.assertThat

private val listServerUrl = "http://localhost:8182"
private val messageServerUrl = "http://localhost:8181"
private val mapper = objectMapper()

class IntegrationTest : Test({

    val listServer = ListServer(8182)
    val messageServer = MessageServer(8181)

    before {
        listServer.start()
        messageServer.start()
    }

    after {
        listServer.stop()
        messageServer.stop()
    }

    test("workflow") {
        val message = createMessage("hey there")
        val messageList = createMessageList("What to do")

        addMessageToList(messageList, message)

        val fetchResponse = RestClient().get("$listServerUrl/lists/${messageList.id}")

        assertSuccess(fetchResponse, statusCode = 200) {
            val list = mapper.readValue(it.body, MessageList::class.java)

            assertThat(list.title).isEqualTo("What to do")
            assertThat(list.messages.size).isEqualTo(1)
            assertThat(list.messages.first().id).isEqualTo(message.id!!)
            assertThat(list.messages.first().value).isEqualTo("hey there")
        }
    }
})

private fun addMessageToList(messageList: MessageList, message: Message) {
    val addResponse = RestClient().post("$listServerUrl/lists/${messageList.id}/add/${message.id}")

    assertSuccess(addResponse, statusCode = 201)
}

private fun createMessageList(title: String): MessageList {
    val body = mapper.writeValueAsString(MessageList(title = title))
    val response = RestClient().post("$listServerUrl/lists", body)

    assertSuccess(response, statusCode = 201)
    return mapper.readValue((response as Response.Success).body, MessageList::class.java)
}

private fun createMessage(value: String): Message {
    val body = mapper.writeValueAsString(Message(value = value))
    val response = RestClient().post("$messageServerUrl/messages", body)

    assertSuccess(response, statusCode = 201)
    return mapper.readValue((response as Response.Success).body, Message::class.java)
}
