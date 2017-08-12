package test.tygern.caribou.listserver

import com.tygern.caribou.lists.Message
import com.tygern.caribou.lists.MessageList
import com.tygern.caribou.listserver.ListServer
import com.tygern.caribou.restsupport.Response
import com.tygern.caribou.restsupport.RestClient
import com.tygern.caribou.restsupport.objectMapper
import io.damo.aspen.Test
import io.tygern.caribou.testsupport.assertError
import io.tygern.caribou.testsupport.assertSuccess
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import java.lang.RuntimeException

private val listServerUrl = "http://localhost:8182"
private val mapper = objectMapper()

class ListApiTest : Test({

    val listServer = ListServer(8182)
    val fakeMessageServer = FakeMessageServer(8181)

    before {
        listServer.start()
        fakeMessageServer.start()
    }

    after {
        listServer.stop()
        fakeMessageServer.stop()
    }

    test("create") {
        val body = mapper.writeValueAsString(MessageList(title = "What to do"))


        val response = RestClient().post("$listServerUrl/lists", body)


        assertSuccess(response, statusCode = 201) {
            val createdMessage = mapper.readValue(it.body, MessageList::class.java)

            assertThat(createdMessage.title).isEqualTo("What to do")
            assertThat(createdMessage.messages).isEmpty()
        }
    }

    test("find") {
        val messageList = createList()


        val response = RestClient().get("$listServerUrl/lists/${messageList.id}")


        assertSuccess(response, statusCode = 200) {
            val createdMessage = mapper.readValue(it.body, MessageList::class.java)

            assertThat(createdMessage.title).isEqualTo("What to do")
            assertThat(createdMessage.messages).isEmpty()
        }
    }

    test("find not found") {
        val response = RestClient().get("$listServerUrl/lists/pickles")


        assertError(response, statusCode = 404) {
            assertThat(it.message).isEqualTo("List not found")
        }
    }

    test("add") {
        val messageList = createList()


        val response = RestClient().post("$listServerUrl/lists/${messageList.id}/add/i-exist")


        assertSuccess(response, statusCode = 201) {
            val createdMessage = mapper.readValue(it.body, MessageList::class.java)

            Assertions.assertThat(createdMessage.title).isEqualTo("What to do")
            Assertions.assertThat(createdMessage.messages).containsExactly(Message("i-exist", "I exist"))
        }
    }

    test("add fail") {
        val response = RestClient().post("$listServerUrl/lists/no-luck/add/not-around")


        assertError(response, statusCode = 400)
    }
})

fun createList(): MessageList {
    val body = mapper.writeValueAsString(MessageList(title = "What to do"))


    val response = RestClient().post("$listServerUrl/lists", body)


    return when(response) {
        is Response.Success -> mapper.readValue(response.body, MessageList::class.java)
        is Response.Error -> throw RuntimeException("Message creation failed")
    }
}