package test.tygern.caribou.lists

import com.tygern.caribou.lists.ListRepository
import com.tygern.caribou.lists.Message
import com.tygern.caribou.lists.MessageList
import io.damo.aspen.Test
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat

class ListRepositoryTest : Test({
    val todo = MessageList(title = "Things to do")
    val other = MessageList(title = "Other Stuff")

    val messageClient = FakeMessageClient()

    var repo = ListRepository(messageClient)

    before {
        repo = ListRepository(messageClient)
    }

    test("unique id") {
        val savedTodo = repo.create(todo)
        val savedOther = repo.create(other)

        assertThat(savedTodo.id).isNotNull()
        assertThat(savedTodo.title).isEqualTo("Things to do")
        assertThat(savedTodo.messages).isEmpty()
        assertThat(savedTodo.id).isNotEqualTo(savedOther.id)
    }

    test("find") {
        val savedTodo = repo.create(todo)

        val messageList = repo.find(savedTodo.id!!)

        assertThat(messageList).isEqualTo(savedTodo)
    }

    test("find not found") {
        val messageList = repo.find("bananas")

        assertThat(messageList).isNull()
    }

    test("addMessage") {
        val savedTodo = repo.create(todo)

        repo.addMessage(savedTodo.id!!, "i-exist")


        val list = repo.find(savedTodo.id!!)!!

        assertThat(list.messages).containsExactly(
            Message("i-exist", "I exist")
        )
    }

    test("addMessage no list") {
        val result = repo.addMessage("pickels", "i-exist")


        assertThat(result).isNull()
    }

    test("addMessage no message") {
        val savedTodo = repo.create(todo)

        val result = repo.addMessage(savedTodo.id!!, "no-dice")

        assertThat(result).isNull()
    }
})