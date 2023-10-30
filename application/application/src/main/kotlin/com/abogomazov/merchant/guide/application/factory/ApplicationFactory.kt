package com.abogomazov.merchant.guide.application.factory

import com.abogomazov.merchant.guide.application.Application
import com.abogomazov.merchant.guide.application.property.ApplicationProperties
import com.abogomazov.merchant.guide.application.property.UI
import com.abogomazov.merchant.guide.cli.ApplicationShell
import com.abogomazov.merchant.guide.cli.ConsoleIO
import com.abogomazov.merchant.guide.rest.ApplicationServer
import com.abogomazov.merchant.guide.usecase.common.GalaxyNumberEvaluator
import com.abogomazov.merchant.guide.usecase.market.GetResourceMarketPriceUseCase
import com.abogomazov.merchant.guide.usecase.market.SetResourceMarketPriceUseCase
import com.abogomazov.merchant.guide.usecase.translator.GetTranslationUseCase
import com.abogomazov.merchant.guide.usecase.translator.SetTranslationUseCase

class ApplicationFactory(
    private val properties: ApplicationProperties
) {
    private val storageConfiguration = StorageConfiguration(properties)

    fun build(): Application {
        val evaluator = GalaxyNumberEvaluator(
            storageConfiguration.translationProvider,
        )
        val getTranslationUseCase = GetTranslationUseCase(evaluator)
        val setTranslationUseCase = SetTranslationUseCase(
            storageConfiguration.translationProvider,
            storageConfiguration.translationPersister,
            storageConfiguration.translationRemover,
        )
        val getPriceUseCase = GetResourceMarketPriceUseCase(
            evaluator,
            storageConfiguration.marketPriceProvider,
        )
        val setPriceUseCase = SetResourceMarketPriceUseCase(
            evaluator,
            storageConfiguration.marketPricePersister,
            storageConfiguration.marketPriceProvider,
            storageConfiguration.marketPriceRemover,
        )

        return when (properties.application.ui) {
            UI.CLI -> {
                ApplicationShell(
                    getTranslationUseCase = getTranslationUseCase,
                    setTranslationUseCase = setTranslationUseCase,
                    getPriceUseCase = getPriceUseCase,
                    setPriceUseCase = setPriceUseCase,
                    commandSource = ConsoleIO,
                    resultCollector = ConsoleIO
                )
            }
            UI.REST -> {
                ApplicationServer(
                    getTranslationUseCase = getTranslationUseCase,
                    setTranslationUseCase = setTranslationUseCase,
                    getResourcePriceUseCase = getPriceUseCase,
                    setResourcePriceUseCase = setPriceUseCase,
                )
            }
        }
    }
}
