package com.tygern.caribou.restsupport

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.eclipse.jetty.server.Server
import org.slf4j.Logger
import org.slf4j.LoggerFactory


abstract class BaseApp(val port: Int) {
    val logger: Logger = LoggerFactory.getLogger(javaClass)

    protected val server = Server(port).apply {
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
}

fun objectMapper(): ObjectMapper = ObjectMapper().registerKotlinModule().registerModule(JavaTimeModule())
