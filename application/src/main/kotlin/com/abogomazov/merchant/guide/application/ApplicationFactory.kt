package com.abogomazov.merchant.guide.application

import com.abogomazov.merchant.guide.application.io.ConsoleIO
import com.abogomazov.merchant.guide.cli.ApplicationShell
import com.abogomazov.merchant.guide.cli.CommandExecutor
import com.abogomazov.merchant.guide.usecase.common.GalaxyNumberEvaluator
import com.abogomazov.merchant.guide.usecase.common.TranslationProvider
import com.abogomazov.merchant.guide.usecase.market.GetResourceMarketPriceUseCase
import com.abogomazov.merchant.guide.usecase.market.MarketPricePersister
import com.abogomazov.merchant.guide.usecase.market.MarketPriceProvider
import com.abogomazov.merchant.guide.usecase.market.SetResourceMarketPriceUseCase
import com.abogomazov.merchant.guide.usecase.translator.GetTranslationUseCase
import com.abogomazov.merchant.guide.usecase.translator.SetTranslationUseCase
import com.abogomazov.merchant.guide.usecase.translator.TranslationPersister
import com.abogomazov.merchant.guide.usecase.translator.TranslationRemover

private const val DEFAULT_MODE = "cli"

class ApplicationFactory(
    private val translationProvider: TranslationProvider,
    private val translationPersister: TranslationPersister,
    private val translationRemover: TranslationRemover,
    private val marketPriceProvider: MarketPriceProvider,
    private val marketPricePersister: MarketPricePersister,
) {

    fun build(mode: String = DEFAULT_MODE): Application {
        val evaluator = GalaxyNumberEvaluator(translationProvider)
        val getTranslationUseCase = GetTranslationUseCase(evaluator)
        val setTranslationUseCase = SetTranslationUseCase(
            translationProvider,
            translationPersister,
            translationRemover
        )
        val getPriceUseCase = GetResourceMarketPriceUseCase(evaluator, marketPriceProvider)
        val setPriceUseCase = SetResourceMarketPriceUseCase(evaluator, marketPricePersister)

        return when (mode) {
            "cli" -> {
                val io = ConsoleIO()
                val commandExecutor = CommandExecutor(
                    getTranslationUseCase,
                    setTranslationUseCase,
                    setPriceUseCase,
                    getPriceUseCase
                )
                ApplicationShell(
                    commandExecutor = commandExecutor,
                    commandSource = io,
                    resultCollector = io
                )
            }
            else -> error("$mode is not supported")
        }
    }
}

interface Application {
    fun run()
}
