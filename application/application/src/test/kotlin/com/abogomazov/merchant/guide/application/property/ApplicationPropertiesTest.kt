package com.abogomazov.merchant.guide.application.property

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class ApplicationPropertiesTest : FreeSpec({

    "load test properties" {
        val sut = ApplicationProperties("test")

        with(sut) {
            application shouldBe ApplicationProperty(UI.REST, Storage.POSTGRES)
            db shouldBe PostgresProperty("test-url", "user", "pass")
        }
    }

    "db properties are optional until access" {
        val sut = ApplicationProperties("lazy")

        sut.application shouldBe ApplicationProperty(UI.CLI, Storage.POSTGRES)

        // Should not happen but if it does throw exception
        shouldThrow<IllegalStateException> {
            sut.db
        }.message shouldBe "postgres-lazy.properties is required"
    }

    "application properties are always required, fail on construction" {
        shouldThrow<IllegalStateException> {
            ApplicationProperties("prod")
        }.message shouldBe "application-prod.properties is required"
    }
})
