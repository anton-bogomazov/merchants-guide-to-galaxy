package com.abogomazov.merchant.guide.domain.galaxy

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FreeSpec
import io.kotest.datatest.withData

class GalaxyNumeralTest : FreeSpec({
    "any one worded string of letters is allowed" - {
        withData(
            nameFn = { "blank string: <$it>" },
            "peep", "BoOp", "x"
        ) {
            GalaxyNumeral.from(it).shouldBeRight()
        }
    }

    "impossible to create digit from blank string" - {
        withData(
            nameFn = { "blank string: <$it>" },
            "", "  ", " \n "
        ) {
            GalaxyNumeral.from(it).shouldBeLeft(GalaxyNumeralValidationError.EmptyString)
        }
    }

    "impossible to create digit from non-letter string" - {
        withData(
            "123", "He11o", "$"
        ) {
            GalaxyNumeral.from(it).shouldBeLeft(GalaxyNumeralValidationError.NonLetters)
        }
    }

    "impossible to create digit from string with more than one word" - {
        GalaxyNumeral.from("foo bar").shouldBeLeft(GalaxyNumeralValidationError.MoreThanOneWord)
    }
})
