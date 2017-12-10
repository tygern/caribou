package test.tygern.caribou.messages

import com.tygern.caribou.messages.Message
import com.tygern.caribou.messages.MessageRepository
import io.damo.aspen.Test

import org.assertj.core.api.Assertions.assertThat

class MessageRepositoryTest : Test({
    val hi = Message(value = "hi there")
    val bye = Message(value = "bye there")

    var repo = MessageRepository()

    before {
        repo = MessageRepository()
    }

    test("unique id") {
        val savedHi = repo.create(hi)
        val savedBye = repo.create(bye)

        assertThat(savedHi.id).isNotNull()
        assertThat(savedHi.value).isEqualTo("hi there")
        assertThat(savedHi.id).isNotEqualTo(savedBye.id)
    }

    test("find") {
        val savedHi = repo.create(hi)
        val retrievedHi = repo.find(savedHi.id!!)

        assertThat(retrievedHi).isEqualTo(savedHi)
    }

    test("find not found") {
        val retrievedHi = repo.find("chicken")

        assertThat(retrievedHi).isNull()
    }

    test("list") {
        val savedHi = repo.create(hi)
        val savedBye = repo.create(bye)


        val retrievedMessages = repo.list()


        assertThat(retrievedMessages).containsExactlyInAnyOrder(savedHi, savedBye)
    }

    test("delete") {
        val savedHi = repo.create(hi)
        val savedBye = repo.create(bye)

        repo.delete(savedHi.id!!)

        assertThat(repo.list()).containsExactly(savedBye)

        repo.delete("chicken")

        assertThat(repo.list()).containsExactly(savedBye)
    }

    test("delete and find") {
        val message = repo.create(hi)
        val messageId = message.id!!

        repo.delete(messageId)

        assertThat(repo.find(messageId)).isEqualTo(message.copy(deleted = true))
    }

    test("update") {
        val savedHi = repo.create(hi)

        val updatedHi = repo.update(savedHi.id!!, Message(value = "edited"))
        val retrievedHi = repo.find(savedHi.id!!)

        assertThat(updatedHi).isEqualTo(retrievedHi)
        assertThat(updatedHi!!.value).isEqualTo("edited")
    }

    test("update not found") {
        val updatedMessage = repo.update("chicken", Message(value = "not used"))

        assertThat(updatedMessage).isNull()
    }
})
