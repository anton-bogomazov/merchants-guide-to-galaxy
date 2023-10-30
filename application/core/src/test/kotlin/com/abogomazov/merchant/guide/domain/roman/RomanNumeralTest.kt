package com.abogomazov.merchant.guide.domain.roman

import com.abogomazov.merchant.guide.domain.romanNumeralStrings
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FreeSpec
import io.kotest.datatest.withData

class RomanNumeralTest : FreeSpec({
    "RomanNumeral can be created from its string representation" - {
        withData(
            romanNumeralStrings() +
            romanNumeralStrings().map { it.lowercase() },
        ) { str: String ->
            str.toRomanNumeral().shouldBeRight()
        }
    }

    "RomanNumeral cannot be created from anything else" {
        "1".toRomanNumeral().shouldBeLeft(
            RomanNumeralConstructionError("1")
        )
    }
})
