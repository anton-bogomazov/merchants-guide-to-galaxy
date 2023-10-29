package com.abogomazov.merchant.guide.application.factory

import com.abogomazov.merchant.guide.application.Application
import com.abogomazov.merchant.guide.application.property.ApplicationProperties
import com.abogomazov.merchant.guide.application.property.Storage
import com.abogomazov.merchant.guide.application.property.UI
import com.abogomazov.merchant.guide.cli.ApplicationShell
import com.abogomazov.merchant.guide.cli.ConsoleIO
import com.abogomazov.merchant.guide.rest.ApplicationServer
import com.abogomazov.merchant.guide.storage.inmemory.MarketInMemoryRepository
import com.abogomazov.merchant.guide.storage.inmemory.TranslationInMemoryRepository
import com.abogomazov.merchant.guide.storage.postgres.PostgresDatasource
import com.abogomazov.merchant.guide.storage.postgres.repository.MarketPostgresRepository
import com.abogomazov.merchant.guide.storage.postgres.repository.TranslationPostgresRepository
import com.abogomazov.merchant.guide.usecase.common.GalaxyNumberEvaluator
import com.abogomazov.merchant.guide.usecase.contracts.MarketRepository
import com.abogomazov.merchant.guide.usecase.contracts.TranslationRepository
import com.abogomazov.merchant.guide.usecase.market.GetResourceMarketPriceUseCase
import com.abogomazov.merchant.guide.usecase.market.SetResourceMarketPriceUseCase
import com.abogomazov.merchant.guide.usecase.translator.GetTranslationUseCase
import com.abogomazov.merchant.guide.usecase.translator.SetTranslationUseCase

class ApplicationFactory(
    private val properties: ApplicationProperties
) {
    private val postgresDatabase by lazy {
        PostgresDatasource(
            jdbcUrl = properties.db.jdbcUrl,
            username = properties.db.username,
            password = properties.db.password,
        )
    }

    fun build(): Application {
        val marketStorage = marketStorage(properties.application.storage)
        val translationStorage = translationStorage(properties.application.storage)

        val evaluator = GalaxyNumberEvaluator(translationStorage)
        val getTranslationUseCase = GetTranslationUseCase(evaluator)
        val setTranslationUseCase = SetTranslationUseCase(translationStorage, translationStorage, translationStorage)
        val getPriceUseCase = GetResourceMarketPriceUseCase(evaluator, marketStorage)
        val setPriceUseCase = SetResourceMarketPriceUseCase(evaluator, marketStorage)

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

    private fun marketStorage(type: Storage): MarketRepository {
        return when (type) {
            Storage.INMEMORY -> MarketInMemoryRepository()
            Storage.POSTGRES -> MarketPostgresRepository(postgresDatabase)
        }
    }

    private fun translationStorage(type: Storage): TranslationRepository {
        return when (type) {
            Storage.INMEMORY -> TranslationInMemoryRepository()
            Storage.POSTGRES -> TranslationPostgresRepository(postgresDatabase)
        }
    }
}
