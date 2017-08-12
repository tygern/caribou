package test.tygern.caribou.messageserver

import com.tygern.caribou.messages.Message
import com.tygern.caribou.messageserver.MessageServer
import com.tygern.caribou.restsupport.Response
import com.tygern.caribou.restsupport.RestClient
import com.tygern.caribou.restsupport.objectMapper
import io.damo.aspen.Test
import io.tygern.caribou.testsupport.assertError
import io.tygern.caribou.testsupport.assertSuccess
import org.assertj.core.api.Assertions.assertThat
import java.lang.RuntimeException

private val messageServerUrl = "http://localhost:8181"
private val mapper = objectMapper()

class MessageApiTest : Test({

    val messageServer = MessageServer(8181)

    before { messageServer.start() }
    after { messageServer.stop() }

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
        val messageInList = createMessage()


        val response = RestClient().get("$messageServerUrl/messages")


        assertSuccess(response, statusCode = 200) {
            val messageListType = mapper.typeFactory.constructCollectionType(List::class.java, Message::class.java)
            val messageList: List<Message> = mapper.readValue(it.body, messageListType)

            assertThat(messageList).contains(messageInList)
        }
    }

    test("read") {
        val message = createMessage()


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
        val message = createMessage()


        val response = RestClient().delete("$messageServerUrl/messages/${message.id}")


        assertSuccess(response, statusCode = 204)
        assertError(RestClient().get("$messageServerUrl/messages/${message.id}"), statusCode = 404)
    }

    test("update") {
        val message = createMessage()
        val body = mapper.writeValueAsString(Message(value = "hola", id = "could be nonsense"))


        val response = RestClient().put("$messageServerUrl/messages/${message.id}", body)


        assertSuccess(response, statusCode = 200) {
            val createdMessage = mapper.readValue(it.body, Message::class.java)

            assertThat(createdMessage.value).isEqualTo("hola")
            assertThat(createdMessage.id).isEqualTo(message.id)
        }
    }

    test("update not found") {
        val body = mapper.writeValueAsString(Message(value = "hola", id = "could be nonsense"))


        val response = RestClient().put("$messageServerUrl/messages/pickles", body)


        assertError(response, statusCode = 404) {
            assertThat(it.message).isEqualTo("Message not found")
        }
    }
})

fun createMessage(): Message {
    val body = mapper.writeValueAsString(Message(value = "hey"))


    val response = RestClient().post("$messageServerUrl/messages", body)


    return when (response) {
        is Response.Success -> mapper.readValue(response.body, Message::class.java)
        is Response.Error -> throw RuntimeException("Message creation failed")
    }
}
