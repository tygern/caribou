package io.tygern.caribou.testsupport

import com.tygern.caribou.restsupport.Response
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail

fun assertSuccess(response: Response, statusCode: Int, assertions: (Response.Success) -> Unit) = when (response) {
    is Response.Error -> fail("Request failed unexpectedly: $response")
    is Response.Success -> {
        assertThat(response.statusCode).isEqualTo(statusCode)
        assertions(response)
    }
}

fun assertError(response: Response, statusCode: Int, assertions: (Response.Error) -> Unit) = when (response) {
    is Response.Success -> fail("Request succeeded unexpectedly: $response")
    is Response.Error -> {
        assertThat(response.statusCode).isEqualTo(statusCode)
        assertions(response)
    }
}
