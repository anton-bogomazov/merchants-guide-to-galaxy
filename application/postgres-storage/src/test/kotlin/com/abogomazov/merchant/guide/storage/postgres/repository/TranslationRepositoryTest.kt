package com.abogomazov.merchant.guide.storage.postgres.repository

import com.abogomazov.merchant.guide.domain.galaxyNumeral
import com.abogomazov.merchant.guide.domain.roman.RomanNumeral
import com.abogomazov.merchant.guide.storage.postgres.PostgresContainer
import com.abogomazov.merchant.guide.storage.postgres.postgresDatasource
import com.abogomazov.merchant.guide.storage.postgres.runMigration
import com.abogomazov.merchant.guide.storage.postgres.translationRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import org.postgresql.util.PSQLException

class TranslationRepositoryTest : FreeSpec({
    extension(PostgresContainer)

    beforeTest {
        runMigration(
            postgresDatasource(PostgresContainer.container)
        )
    }

    "saved association can be restored by galaxy or roman numeral" {
        val repository = translationRepository(PostgresContainer.container)
        val galaxyNumeral = galaxyNumeral("glob")
        val romanNumeral = RomanNumeral.D

        repository.associate(galaxyNumeral, romanNumeral)

        repository.getTranslation(galaxyNumeral) shouldBe romanNumeral
        repository.getTranslation(romanNumeral) shouldBe galaxyNumeral
    }

    "saved association can be removed" {
        val repository = translationRepository(PostgresContainer.container)
        val galaxyNumeral = galaxyNumeral("glob")
        val romanNumeral = RomanNumeral.D

        repository.associate(galaxyNumeral, romanNumeral)

        repository.remove(galaxyNumeral, romanNumeral)
        repository.getTranslation(romanNumeral) shouldBe null
    }

    "same galaxy numeral can't be associated with 2 different roman numerals" {
        val repository = translationRepository(PostgresContainer.container)
        val galaxyNumeral = galaxyNumeral("glob")

        repository.associate(galaxyNumeral, RomanNumeral.D)

        shouldThrow<PSQLException> {
            repository.associate(galaxyNumeral, RomanNumeral.I)
        }
    }

    "same roman numeral can't be associated with 2 different galaxy numerals" {
        val repository = translationRepository(PostgresContainer.container)
        val romanNumeral = RomanNumeral.D

        repository.associate(galaxyNumeral("glob"), romanNumeral)

        shouldThrow<PSQLException> {
            repository.associate(galaxyNumeral("drop"), romanNumeral)
        }
    }
})
