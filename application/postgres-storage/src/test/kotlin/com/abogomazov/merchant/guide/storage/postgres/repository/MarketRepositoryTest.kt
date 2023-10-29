package com.abogomazov.merchant.guide.storage.postgres.repository

import com.abogomazov.merchant.guide.domain.price
import com.abogomazov.merchant.guide.domain.resource
import com.abogomazov.merchant.guide.storage.postgres.PostgresContainer
import com.abogomazov.merchant.guide.storage.postgres.marketRepository
import com.abogomazov.merchant.guide.storage.postgres.postgresDatasource
import com.abogomazov.merchant.guide.storage.postgres.runMigration
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import org.postgresql.util.PSQLException
import java.math.BigDecimal

class MarketRepositoryTest : FreeSpec({
    extension(PostgresContainer)

    beforeTest {
        runMigration(
            postgresDatasource(PostgresContainer.container)
        )
    }

    "price for resource can be saved to and restored from db" {
        val repository = marketRepository(PostgresContainer.container)
        val resource = resource("gold")
        val unitPrice = price(BigDecimal("1234567890.12345678901234567891"))

        repository.setPrice(resource, unitPrice)

        repository.getUnitPrice(resource) shouldBe unitPrice
    }

    "saving price for the same resource is not allowed" {
        val repository = marketRepository(PostgresContainer.container)
        val resource = resource("gold")
        val unitPrice = price(43.1)

        repository.setPrice(resource, unitPrice)

        shouldThrow<PSQLException> {
            repository.setPrice(resource, price(1.0))
        }
    }
})
