package com.abogomazov.merchant.guide

import com.abogomazov.merchant.guide.application.factory.ApplicationFactory
import com.abogomazov.merchant.guide.application.property.ApplicationProperties
import org.slf4j.LoggerFactory

fun main() {
    val logger = LoggerFactory.getLogger("com.abogomazov.merchant.guide.MainKt")
    val profile = System.getenv("APP_PROFILE") ?: error("Set APP_PROFILE env")

    val properties = ApplicationProperties(profile)
    val (ui, storage) = properties.application

    ApplicationFactory(properties)
        .also { logger.info("Application will be constructed with ui=$ui and storage=$storage") }
        .build().also { logger.info("Application constructed") }
        .run()
}
