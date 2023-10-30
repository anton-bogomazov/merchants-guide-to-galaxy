package com.abogomazov.merchant.guide.application.factory

import com.abogomazov.merchant.guide.application.property.ApplicationProperties
import com.abogomazov.merchant.guide.storage.inmemory.MarketInMemoryRepository
import com.abogomazov.merchant.guide.storage.inmemory.TranslationInMemoryRepository
import com.abogomazov.merchant.guide.storage.postgres.repository.MarketPostgresRepository
import com.abogomazov.merchant.guide.storage.postgres.repository.TranslationPostgresRepository
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.kotest.matchers.types.shouldBeTypeOf

class StorageConfigurationTest : FreeSpec({

    "different data interface implementors shares the same instance of inmemory repos" {
        val sut = StorageConfiguration(ApplicationProperties("safe"))

        sut.translationProvider.shouldBeTypeOf<TranslationInMemoryRepository>()
        sut.translationProvider shouldBeSameInstanceAs sut.translationPersister
        sut.translationPersister shouldBeSameInstanceAs sut.translationRemover

        sut.marketPricePersister.shouldBeTypeOf<MarketInMemoryRepository>()
        sut.marketPricePersister shouldBeSameInstanceAs sut.marketPriceProvider
        sut.marketPriceProvider shouldBeSameInstanceAs sut.marketPriceRemover
    }

    "different data interface implementors shares the same instance of postgres repos" {
        val sut = StorageConfiguration(ApplicationProperties("test"))

        sut.translationProvider.shouldBeTypeOf<TranslationPostgresRepository>()
        sut.translationProvider shouldBeSameInstanceAs sut.translationPersister
        sut.translationPersister shouldBeSameInstanceAs sut.translationRemover

        sut.marketPricePersister.shouldBeTypeOf<MarketPostgresRepository>()
        sut.marketPricePersister shouldBeSameInstanceAs sut.marketPriceProvider
        sut.marketPriceProvider shouldBeSameInstanceAs sut.marketPriceRemover
    }
})
