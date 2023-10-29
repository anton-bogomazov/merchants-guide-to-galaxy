package com.abogomazov.merchant.guide

import com.abogomazov.merchant.guide.application.ApplicationFactory
import org.slf4j.LoggerFactory
import java.util.*

fun main() {
    val logger = LoggerFactory.getLogger("com.abogomazov.merchant.guide.MainKt")

    val properties = properties("application")
    val ui = UI.valueOf(properties.getProperty("application.ui")?.uppercase() ?: "cli".uppercase())
    val storage = Storage.valueOf(properties.getProperty("application.storage")?.uppercase() ?: "inmemory".uppercase())

    logger.info("Application will be ran with ui=$ui and storage=$storage")

    logger.info("Dependencies instantiated, constructing application")
    val app = ApplicationFactory(
        ui = ui,
        storage = storage
    ).build()
    logger.info("Application constructed")

    app.run()
}

enum class UI { CLI, REST }
enum class Storage { INMEMORY, POSTGRES }

fun properties(name: String): Properties {
    val configFile = loadResource("/$name.properties")
    val props = Properties()
    props.load(configFile)

    return props
}

private fun loadResource(path: String) =
    ApplicationFactory::class.java.getResourceAsStream(path)
        ?: error("Can't load resource")
