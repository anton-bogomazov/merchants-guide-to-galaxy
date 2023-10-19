package com.abogomazov.merchant.guide.application

import com.abogomazov.merchant.guide.cli.CommandExecutor
import com.abogomazov.merchant.guide.application.inmemory.InMemoryMarket
import com.abogomazov.merchant.guide.application.inmemory.InMemoryTranslationRegistry
import com.abogomazov.merchant.guide.usecase.common.LocalNumberEvaluator
import com.abogomazov.merchant.guide.usecase.common.TranslationProvider
import com.abogomazov.merchant.guide.usecase.translator.GetTranslationUseCase
import com.abogomazov.merchant.guide.usecase.translator.SetTranslationUseCase
import com.abogomazov.merchant.guide.usecase.market.GetResourceMarketPriceUseCase
import com.abogomazov.merchant.guide.usecase.market.MarketPricePersister
import com.abogomazov.merchant.guide.usecase.market.MarketPriceProvider
import com.abogomazov.merchant.guide.usecase.market.SetResourceMarketPriceUseCase
import com.abogomazov.merchant.guide.usecase.translator.TranslationPersister

class Application(
    private val translationProvider: TranslationProvider,
    private val translationPersister: TranslationPersister,
    private val marketPriceProvider: MarketPriceProvider,
    private val marketPricePersister: MarketPricePersister,
    private val commandSource: CommandSource,
    private val resultCollector: ResultCollector,
    private val parserFactory: ParserFactory,
) {
    fun build(): ApplicationShell {
        val evaluator = LocalNumberEvaluator(translationProvider)

        return ApplicationShell(
            commandSource = commandSource,
            resultCollector = resultCollector,
            commandParserFactory = parserFactory,
            commandExecutor = CommandExecutor(
                getTranslation = GetTranslationUseCase(evaluator),
                setTranslation = SetTranslationUseCase(translationProvider, translationPersister),
                getPrice = GetResourceMarketPriceUseCase(evaluator, marketPriceProvider),
                setPrice = SetResourceMarketPriceUseCase(evaluator, marketPricePersister)
            )
        )
    }
}

fun main() {
    val dictionary = InMemoryTranslationRegistry()
    val market = InMemoryMarket()
    val io = ConsoleIO()
    val parserFactory = ParserFactory()

    Application(
        translationPersister = dictionary,
        translationProvider = dictionary,
        marketPricePersister = market,
        marketPriceProvider = market,
        resultCollector = io,
        commandSource = io,
        parserFactory = parserFactory
    ).build().run()
}
