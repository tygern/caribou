package test.tygern.caribou.messageserver

import com.fasterxml.jackson.databind.ObjectMapper
import com.tygern.caribou.messages.Message
import com.tygern.caribou.messageserver.App
import com.tygern.caribou.restsupport.Response
import com.tygern.caribou.restsupport.RestClient
import io.damo.aspen.Test
import io.tygern.caribou.testsupport.assertError
import io.tygern.caribou.testsupport.assertSuccess
import org.assertj.core.api.Assertions.assertThat
import java.lang.RuntimeException

private val messageServerUrl = "http://localhost:8181"

class MessagesTest : Test({

    val app = App(8181)
    val mapper = app.mapper

    before { app.start() }
    after { app.stop() }

    test("create") {
        val body = mapper.writeValueAsString(Message(value = "hey"))


        val response = RestClient().post("$messageServerUrl/messages", body)


        assertSuccess(response, statusCode = 201) {
            val createdMessage = mapper.readValue(it.body, Message::class.java)

            assertThat(createdMessage.value).isEqualTo("hey")
            assertThat(createdMessage.id).isNotNull()
        }
    }

    test("list") {
        val messageInList = createMessage(mapper)


        val response = RestClient().get("$messageServerUrl/messages")


        assertSuccess(response, statusCode = 200) {
            val messageListType = mapper.typeFactory.constructCollectionType(List::class.java, Message::class.java)
            val messageList: List<Message> = mapper.readValue(it.body, messageListType)

            assertThat(messageList).contains(messageInList)
        }
    }

    test("read") {
        val message = createMessage(mapper)


        val response = RestClient().get("$messageServerUrl/messages/${message.id}")


        assertSuccess(response, statusCode = 200) {
            val readMessage = mapper.readValue(it.body, Message::class.java)

            assertThat(readMessage).isEqualTo(readMessage)
        }
    }

    test("read not found") {
        val response = RestClient().get("$messageServerUrl/messages/pickles")


        assertError(response, statusCode = 404) {
            assertThat(it.message).isEqualTo("Message not found")
        }
    }

    test("delete") {
        val message = createMessage(mapper)


        val response = RestClient().delete("$messageServerUrl/messages/${message.id}")


        assertSuccess(response, statusCode = 204)
        assertError(RestClient().get("$messageServerUrl/messages/${message.id}"), statusCode = 404)
    }
})

fun createMessage(mapper: ObjectMapper): Message {
    val body = mapper.writeValueAsString(Message(value = "hey"))


    val response = RestClient().post("$messageServerUrl/messages", body)


    return when(response) {
        is Response.Success -> mapper.readValue(response.body, Message::class.java)
        is Response.Error -> throw RuntimeException("Message creation failed")
    }
}
