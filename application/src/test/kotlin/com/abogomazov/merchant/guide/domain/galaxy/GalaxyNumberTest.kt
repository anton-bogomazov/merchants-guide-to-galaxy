package com.abogomazov.merchant.guide.domain.galaxy

import com.abogomazov.merchant.guide.domain.five
import com.abogomazov.merchant.guide.domain.one
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FreeSpec
import io.kotest.datatest.withData

class GalaxyNumberTest : FreeSpec({
    "galaxy number could be created with a sequence of galaxy numerals" - {
        withData(
            listOf(one()),
            listOf(one(), five()),
            List(1000) { one() },
        ) {
            GalaxyNumber.from(it).shouldBeRight()
        }
    }

    "impossible to create number from empty list of digits" {
        GalaxyNumber.from(emptyList()).shouldBeLeft(GalaxyNumberValidationError.NoDigits)
    }
})
