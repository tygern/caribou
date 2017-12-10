package test.tygern.caribou.lists

import com.tygern.caribou.lists.ListRepository
import com.tygern.caribou.lists.ListService
import com.tygern.caribou.lists.Message
import com.tygern.caribou.lists.MessageList
import io.damo.aspen.Test
import org.assertj.core.api.Assertions.assertThat

class ListServiceTest : Test({
    val todo = MessageList(title = "Things to do")
    val other = MessageList(title = "Other Stuff")

    val messageClient = FakeMessageClient()

    var service = ListService(messageClient, ListRepository())

    before {
        service = ListService(messageClient, ListRepository())
    }

    test("create") {
        val savedTodo = service.create(todo)
        val savedOther = service.create(other)

        assertThat(savedTodo.id).isNotNull()
        assertThat(savedTodo.title).isEqualTo("Things to do")
        assertThat(savedTodo.messages).isEmpty()
        assertThat(savedTodo.id).isNotEqualTo(savedOther.id)
    }

    test("find") {
        val savedTodo = service.create(todo)

        val messageList = service.find(savedTodo.id!!)

        assertThat(messageList).isEqualTo(savedTodo)
    }

    test("find not found") {
        val messageList = service.find("bananas")

        assertThat(messageList).isNull()
    }

    test("addMessage") {
        val savedTodo = service.create(todo)

        service.addMessage(savedTodo.id!!, "i-exist")


        val list = service.find(savedTodo.id!!)!!

        assertThat(list.messages).containsExactly(
            Message("i-exist", "I exist")
        )
    }

    test("addMessage deleted") {
        val savedTodo = service.create(todo)

        val result = service.addMessage(savedTodo.id!!, "deleted")

        assertThat(result).isNull()
    }

    test("addMessage no list") {
        val result = service.addMessage("pickels", "i-exist")


        assertThat(result).isNull()
    }

    test("addMessage no message") {
        val savedTodo = service.create(todo)

        val result = service.addMessage(savedTodo.id!!, "no-dice")

        assertThat(result).isNull()
    }
})