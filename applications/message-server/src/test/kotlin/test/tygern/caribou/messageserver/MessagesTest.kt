package test.tygern.caribou.messageserver

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.tygern.caribou.messages.Message
import com.tygern.caribou.messageserver.App
import com.tygern.caribou.restsupport.RestClient
import io.damo.aspen.Test

import org.assertj.core.api.Assertions.assertThat

class MessagesTest : Test({

    val app = App(8181)
    val mapper = app.mapper

    before { app.start() }
    after { app.stop() }

    test("create") {
        val body = mapper.writeValueAsString(Message(value = "hey"))


        val response = RestClient().post("http://localhost:8181/messages", body)


        val createdMessage = mapper.readValue(response, Message::class.java)

        assertThat(createdMessage.value).isEqualTo("hey")
        assertThat(createdMessage.id).isNotNull()
    }

    test("list") {
        val messageInList = createMessage(mapper)


        val response = RestClient().get("http://localhost:8181/messages")


        val messageListType = mapper.typeFactory.constructCollectionType(List::class.java, Message::class.java)
        val messageList: List<Message> = mapper.readValue(response, messageListType)

        assertThat(messageList).contains(messageInList)
    }

    test("read") {
        val messageInList = createMessage(mapper)


        val response = RestClient().get("http://localhost:8181/messages/" + messageInList.id)


        val message = mapper.readValue(response, Message::class.java)

        assertThat(message).isEqualTo(messageInList)
    }
})

fun createMessage(mapper: ObjectMapper): Message {
    val body = mapper.writeValueAsString(Message(value = "hey"))

    RestClient().post("http://localhost:8181/messages", body).let {
        return mapper.readValue(it, Message::class.java)
    }
}
