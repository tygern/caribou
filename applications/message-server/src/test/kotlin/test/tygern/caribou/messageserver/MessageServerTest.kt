package test.tygern.caribou.messageserver

import com.tygern.caribou.messageserver.MessageServer
import com.tygern.caribou.restsupport.RestClient
import io.damo.aspen.Test
import io.tygern.caribou.testsupport.assertSuccess

import org.assertj.core.api.Assertions.assertThat

class MessageServerTest : Test({

    val messageServer = MessageServer(8181)
    before { messageServer.start() }
    after { messageServer.stop() }

    test {
        val response = RestClient().get("http://localhost:8181")

        assertSuccess(response, statusCode = 200) {
            assertThat(it.body).isEqualTo("Caribou")
        }
    }
})