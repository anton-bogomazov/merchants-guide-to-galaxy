package com.abogomazov.merchant.guide

import com.abogomazov.merchant.guide.application.factory.ApplicationFactory
import com.abogomazov.merchant.guide.application.property.ApplicationProperties
import org.slf4j.LoggerFactory

private const val PROFILE_ENV = "APP_PROFILE"

fun main() {
    val logger = LoggerFactory.getLogger("com.abogomazov.merchant.guide.MainKt")

    val properties = ApplicationProperties(System.getenv(PROFILE_ENV) ?: "")
    val (ui, storage) = properties.application
    logger.info("Application will be constructed with ui=$ui and storage=$storage")

    ApplicationFactory(properties)
        .build().also { logger.info("Application constructed") }
        .run()
}
