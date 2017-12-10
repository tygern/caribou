package test.tygern.caribou.lists

import com.fasterxml.jackson.databind.ObjectMapper
import com.tygern.caribou.lists.Message
import com.tygern.caribou.lists.MessageClient
import com.tygern.caribou.restsupport.RestClient

class FakeMessageClient : MessageClient("", RestClient(), ObjectMapper()) {
    override fun find(id: String) =
        when (id) {
            "i-exist" -> Message(id = "i-exist", value = "I exist")
            "deleted" -> Message(id = "deleted", value = "Deleted", deleted = true)
            else -> null
        }
}