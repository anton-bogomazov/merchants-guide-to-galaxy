package com.abogomazov.merchant.guide.application.factory

import com.abogomazov.merchant.guide.application.property.ApplicationProperties
import com.abogomazov.merchant.guide.application.property.Storage
import com.abogomazov.merchant.guide.storage.inmemory.MarketInMemoryRepository
import com.abogomazov.merchant.guide.storage.inmemory.TranslationInMemoryRepository
import com.abogomazov.merchant.guide.storage.postgres.PostgresDatasource
import com.abogomazov.merchant.guide.storage.postgres.repository.MarketPostgresRepository
import com.abogomazov.merchant.guide.storage.postgres.repository.TranslationPostgresRepository
import com.abogomazov.merchant.guide.usecase.common.TranslationProvider
import com.abogomazov.merchant.guide.usecase.market.MarketPricePersister
import com.abogomazov.merchant.guide.usecase.market.MarketPriceProvider
import com.abogomazov.merchant.guide.usecase.market.MarketPriceRemover
import com.abogomazov.merchant.guide.usecase.translator.TranslationPersister
import com.abogomazov.merchant.guide.usecase.translator.TranslationRemover

class StorageConfiguration(
    private val properties: ApplicationProperties
) {
    private val type = properties.application.storage

    private val postgresDatabase by lazy {
        PostgresDatasource(
            jdbcUrl = properties.db.jdbcUrl,
            username = properties.db.username,
            password = properties.db.password,
        )
    }

    private val marketInMemoryRepository by lazy { MarketInMemoryRepository() }
    private val translationInMemoryRepository by lazy { TranslationInMemoryRepository() }
    private val marketPostgresRepository by lazy { MarketPostgresRepository(postgresDatabase) }
    private val translationPostgresRepository by lazy { TranslationPostgresRepository(postgresDatabase) }

    private fun marketStorage(): Any {
        return when (type) {
            Storage.INMEMORY -> marketInMemoryRepository
            Storage.POSTGRES -> marketPostgresRepository
        }
    }

    private fun translationStorage(): Any {
        return when (type) {
            Storage.INMEMORY -> translationInMemoryRepository
            Storage.POSTGRES -> translationPostgresRepository
        }
    }

    val marketPriceProvider: MarketPriceProvider = marketStorage() as MarketPriceProvider
    val marketPriceRemover: MarketPriceRemover = marketStorage() as MarketPriceRemover
    val marketPricePersister: MarketPricePersister = marketStorage() as MarketPricePersister

    val translationProvider: TranslationProvider = translationStorage() as TranslationProvider
    val translationRemover: TranslationRemover = translationStorage() as TranslationRemover
    val translationPersister: TranslationPersister = translationStorage() as TranslationPersister
}
