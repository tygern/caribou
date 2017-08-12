package test.tygern.caribou.listserver

import com.tygern.caribou.listserver.ListServer
import com.tygern.caribou.restsupport.RestClient
import io.damo.aspen.Test
import io.tygern.caribou.testsupport.assertSuccess

import org.assertj.core.api.Assertions.assertThat

class ListServerTest : Test({

    val listServer = ListServer(8182)
    before { listServer.start() }
    after { listServer.stop() }

    test {
        val response = RestClient().get("http://localhost:8182")

        assertSuccess(response, statusCode = 200) {
            assertThat(it.body).isEqualTo("Caribou")
        }
    }
})