package com.abogomazov.merchant.guide

import com.abogomazov.merchant.guide.application.ApplicationFactory
import com.abogomazov.merchant.guide.application.inmemory.InMemoryDictionary
import com.abogomazov.merchant.guide.application.inmemory.InMemoryMarket
import org.slf4j.LoggerFactory

fun main() {
    val logger = LoggerFactory.getLogger("com.abogomazov.merchant.guide.MainKt")

    val dictionary = InMemoryDictionary()
    val market = InMemoryMarket()

    logger.info("Dependencies instantiated, constructing application")
    val app = ApplicationFactory(
        translationPersister = dictionary,
        translationProvider = dictionary,
        translationRemover = dictionary,
        marketPricePersister = market,
        marketPriceProvider = market,
    ).build("web")
    logger.info("Application constructed")

    app.run()
}
