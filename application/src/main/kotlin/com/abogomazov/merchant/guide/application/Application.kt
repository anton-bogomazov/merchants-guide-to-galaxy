package com.abogomazov.merchant.guide.application

import com.abogomazov.merchant.guide.cli.CommandExecutor
import com.abogomazov.merchant.guide.application.inmemory.InMemoryMarket
import com.abogomazov.merchant.guide.application.inmemory.InMemoryTranslationRegistry
import com.abogomazov.merchant.guide.usecase.common.LocalNumberEvaluator
import com.abogomazov.merchant.guide.usecase.translator.GetTranslationUseCase
import com.abogomazov.merchant.guide.usecase.translator.SetTranslationUseCase
import com.abogomazov.merchant.guide.usecase.market.GetResourceMarketPriceUseCase
import com.abogomazov.merchant.guide.usecase.market.SetResourceMarketPriceUseCase

fun Application(): ApplicationShell {
    // Instantiate Core Dependencies
    val dictionary = InMemoryTranslationRegistry()
    val market = InMemoryMarket()

    // Compose Use Cases
    val evaluator = LocalNumberEvaluator(dictionary)

    val getTranslationUseCase = GetTranslationUseCase(evaluator)
    val setTranslationUseCase = SetTranslationUseCase(dictionary, dictionary)
    val getResourceMarketPrice = GetResourceMarketPriceUseCase(evaluator, market)
    val setResourceMarketPrice = SetResourceMarketPriceUseCase(evaluator, market)

    // Instantiate UI Dependencies
    val io = ConsoleIO()
    val commandExecutor = CommandExecutor(
        getTranslation = getTranslationUseCase,
        setTranslation = setTranslationUseCase,
        getPrice = getResourceMarketPrice,
        setPrice = setResourceMarketPrice
    )
    val extractorFactory = ExtractorFactory()

    // Compose UI
    val shell = ApplicationShell(
        argumentExtractorFactory = extractorFactory,
        commandExecutor = commandExecutor,
        reader = io,
        printer = io
    )

    return shell
}

fun main() {
    Application().run()
}
