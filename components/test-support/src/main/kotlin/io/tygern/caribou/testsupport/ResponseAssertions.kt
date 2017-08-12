package io.tygern.caribou.testsupport

import com.tygern.caribou.restsupport.Response
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail

fun assertSuccess(response: Response, statusCode: Int, assertions: ((Response.Success) -> Unit)? = null) =
    when (response) {
        is Response.Error -> fail("Request failed unexpectedly: $response")
        is Response.Success -> {
            assertThat(response.statusCode)
                .withFailMessage("Expected status $statusCode from: $response")
                .isEqualTo(statusCode)
            assertions?.invoke(response)
        }
    }

fun assertError(response: Response, statusCode: Int, assertions: ((Response.Error) -> Unit)? = null) =
    when (response) {
        is Response.Success -> fail("Request succeeded unexpectedly: $response")
        is Response.Error -> {
            assertThat(response.statusCode)
                .withFailMessage("Expected status $statusCode from: $response")
                .isEqualTo(statusCode)
            assertions?.invoke(response)
        }
    }
