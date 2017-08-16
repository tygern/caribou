package test.tygern.caribou.lists

import com.tygern.caribou.lists.ListRepository
import com.tygern.caribou.lists.MessageListRecord
import io.damo.aspen.Test

import org.assertj.core.api.Assertions.assertThat

class ListRepositoryTest: Test({
    var repo = ListRepository()

    before {
        repo = ListRepository()
    }

    test("return value") {
        val list = MessageListRecord(
            id = "the-id",
            title = "Super list",
            messageIds = listOf("123", "abc")
        )

        assertThat(repo.save(list)).isEqualTo(list)
    }

    test("persistence") {
        val list = MessageListRecord(
            id = "the-id",
            title = "Super list",
            messageIds = listOf("123", "abc")
        )

        repo.save(list)

        assertThat(repo.find(list.id!!)).isEqualTo(list)
    }

    test("adds an id") {
        val list = MessageListRecord(
            title = "Super list",
            messageIds = listOf("123", "abc")
        )

        assertThat(repo.save(list).id).isNotNull()
    }
})
