package com.abogomazov.merchant.guide.domain.local

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FreeSpec
import io.kotest.datatest.withData

class LocalDigitTest : FreeSpec({
    "any one worded string of letters is allowed" - {
        withData(
            nameFn = { "blank string: <$it>" },
            "peep","BoOp", "x"
        ) {
            LocalDigit.from(it).shouldBeRight()
        }
    }

    "impossible to create digit from blank string" - {
        withData(
            nameFn = { "blank string: <$it>" },
            "", "  ", " \n "
        ) {
            LocalDigit.from(it).shouldBeLeft(LocalDigitValidationError.EmptyString)
        }
    }

    "impossible to create digit from non-letter string" - {
        withData(
            "123", "He11o", "$"
        ) {
            LocalDigit.from(it).shouldBeLeft(LocalDigitValidationError.NonLetters)
        }
    }

    "impossible to create digit from string with more than one word" - {
        LocalDigit.from("foo bar").shouldBeLeft(LocalDigitValidationError.MoreThanOneWord)
    }
})