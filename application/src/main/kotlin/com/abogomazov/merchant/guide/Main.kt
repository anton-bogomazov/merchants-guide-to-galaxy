package com.abogomazov.merchant.guide

import com.abogomazov.merchant.guide.application.Application
import com.abogomazov.merchant.guide.application.inmemory.InMemoryDictionary
import com.abogomazov.merchant.guide.application.inmemory.InMemoryMarket
import com.abogomazov.merchant.guide.application.io.ConsoleIO
import com.abogomazov.merchant.guide.cli.ParserFactory
import org.slf4j.LoggerFactory

fun main() {
    val logger = LoggerFactory.getLogger("com.abogomazov.merchant.guide.MainKt")

    val dictionary = InMemoryDictionary()
    val market = InMemoryMarket()
    val io = ConsoleIO()
    val parserFactory = ParserFactory()

    logger.info("Dependencies instantiated, constructing application")
    val app = Application(
        translationPersister = dictionary,
        translationProvider = dictionary,
        translationRemover = dictionary,
        marketPricePersister = market,
        marketPriceProvider = market,
        resultCollector = io,
        commandSource = io,
        parserFactory = parserFactory
    ).build()
    logger.info("Application constructed")

    app.run()
}
