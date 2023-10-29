package com.abogomazov.merchant.guide.application

import com.abogomazov.merchant.guide.Storage
import com.abogomazov.merchant.guide.UI
import com.abogomazov.merchant.guide.application.io.ConsoleIO
import com.abogomazov.merchant.guide.cli.ApplicationShell
import com.abogomazov.merchant.guide.properties
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
    private val ui: UI,
    private val storage: Storage,
) {

    fun build(): Application {
        val marketStorage = marketStorage(storage)
        val translationStorage = translationStorage(storage)

        val evaluator = GalaxyNumberEvaluator(translationStorage)
        val getTranslationUseCase = GetTranslationUseCase(evaluator)
        val setTranslationUseCase = SetTranslationUseCase(translationStorage, translationStorage, translationStorage)
        val getPriceUseCase = GetResourceMarketPriceUseCase(evaluator, marketStorage)
        val setPriceUseCase = SetResourceMarketPriceUseCase(evaluator, marketStorage)

        return when (ui) {
            UI.CLI -> {
                val io = ConsoleIO()

                ApplicationShell(
                    getTranslationUseCase = getTranslationUseCase,
                    setTranslationUseCase = setTranslationUseCase,
                    getPriceUseCase = getPriceUseCase,
                    setPriceUseCase = setPriceUseCase,
                    commandSource = io,
                    resultCollector = io
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

fun marketStorage(type: Storage): MarketRepository {
    return when (type) {
        Storage.INMEMORY -> MarketInMemoryRepository()
        Storage.POSTGRES -> MarketPostgresRepository(postgresDatasource())
    }
}

fun translationStorage(type: Storage): TranslationRepository {
    return when (type) {
        Storage.INMEMORY -> TranslationInMemoryRepository()
        Storage.POSTGRES -> TranslationPostgresRepository(postgresDatasource())
    }
}

fun postgresDatasource() = properties("db").let {
    PostgresDatasource(
        jdbcUrl = it.getProperty("postgres.jdbcUrl"),
        username = it.getProperty("postgres.username"),
        password = it.getProperty("postgres.password"),
    )
}
