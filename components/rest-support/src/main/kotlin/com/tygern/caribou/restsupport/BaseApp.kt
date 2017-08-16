package com.tygern.caribou.restsupport

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.eclipse.jetty.server.Handler
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.HandlerList
import org.slf4j.Logger
import org.slf4j.LoggerFactory


abstract class BaseApp(val port: Int) {
    val logger: Logger = LoggerFactory.getLogger(javaClass)

    protected val server = Server(port).apply {
        stopAtShutdown = true
    }

    init {
        server.handler = HandlerList().apply {
            handlers = controllers().toTypedArray()
        }

        Runtime
            .getRuntime()
            .addShutdownHook(Thread(shutdownServer()))
    }

    fun start() {
        logger.info("App started at http://localhost:$port.")
        server.start()
    }

    fun stop() = shutdownServer().invoke()

    abstract fun controllers() : List<Handler>

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
}

fun objectMapper(): ObjectMapper = ObjectMapper().registerKotlinModule().registerModule(JavaTimeModule())
