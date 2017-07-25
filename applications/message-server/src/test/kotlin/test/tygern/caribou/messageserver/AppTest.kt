package test.tygern.caribou.messageserver

import com.tygern.caribou.messageserver.App
import com.tygern.caribou.restsupport.RestClient
import io.damo.aspen.Test

import org.assertj.core.api.Assertions.assertThat

class AppTest : Test({

    val app = App(8181)

    before {
        app.start()
    }

    after {
        app.stop()
    }

    test {
        val response = RestClient().get("http://localhost:8181")

        assertThat(response).isEqualTo("Hello")
    }
})