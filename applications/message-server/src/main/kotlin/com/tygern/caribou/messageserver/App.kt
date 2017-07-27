package com.tygern.caribou.messageserver

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.tygern.caribou.messages.MessageController
import com.tygern.caribou.messages.MessageRepository
import com.tygern.caribou.restsupport.DefaultController
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.HandlerList
import org.slf4j.LoggerFactory
import java.util.TimeZone

class App(val port: Int) {
    val logger = LoggerFactory.getLogger(javaClass)
    val mapper: ObjectMapper = ObjectMapper().registerKotlinModule().registerModule(JavaTimeModule())

    private val server = Server(port).apply {
        handler = handlerList()
        stopAtShutdown = true
    }

    init {
        Runtime
                .getRuntime()
                .addShutdownHook(Thread(shutdownServer()))
    }

    fun start() {
        logger.info("App started at http://localhost:$port.")
        server.start()
    }

    fun stop() = shutdownServer().invoke()

    private fun shutdownServer() = {
        try {
            if (server.isRunning) {
                server.stop()
            }
            logger.info("App shutdown.")
        } catch (e: Exception) {
            logger.info("Error shutting down app.", e)
        }
    }

    private fun handlerList() = HandlerList().apply {
        addHandler(MessageController(mapper, MessageRepository()))
        addHandler(DefaultController())
    }
}

fun main(args: Array<String>) {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    App(8081).start()
}
