package com.abogomazov.merchant.guide.application.factory

import com.abogomazov.merchant.guide.application.property.ApplicationProperties
import com.abogomazov.merchant.guide.cli.ApplicationShell
import com.abogomazov.merchant.guide.rest.ApplicationServer
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import java.lang.IllegalStateException

class ApplicationFactoryTest : FreeSpec({

    "constructs ApplicationServer if application.ui = rest" {
        ApplicationFactory(
            ApplicationProperties("test")
        ).build() is ApplicationServer
    }

    "constructs ApplicationShell if application.ui = cli" {
        ApplicationFactory(
            ApplicationProperties("safe")
        ).build() is ApplicationShell
    }

    "fails if application.storage = postgres and postgres.properties is not provided" {
        val sut = ApplicationFactory(ApplicationProperties("lazy"))
        shouldThrow<IllegalStateException> {
            sut.build()
        }
    }
})
