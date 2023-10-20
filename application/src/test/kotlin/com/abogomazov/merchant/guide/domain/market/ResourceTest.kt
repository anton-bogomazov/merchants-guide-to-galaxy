package com.abogomazov.merchant.guide.domain.market

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FreeSpec
import io.kotest.datatest.withData

class ResourceTest : FreeSpec({

    "any one worded string of letters is allowed" - {
        withData(
            nameFn = { "blank string: <$it>" },
            "Water", "dirt", "X", "BurBon"
        ) {
            Resource.from(it).shouldBeRight()
        }
    }

    "impossible to create resource from blank string" - {
        withData(
            nameFn = { "blank string: <$it>" },
            "", "  ", " \n "
        ) {
            Resource.from(it).shouldBeLeft(ResourceValidationError.EmptyString)
        }
    }

    "impossible to create resource from non-letter string" - {
        withData(
            "123", "He11o", "$"
        ) {
            Resource.from(it).shouldBeLeft(ResourceValidationError.NonLetters)
        }
    }

    "impossible to create resource from string with more than one word" - {
        withData("Salt Water", "Gorges Blue Cheese", "R E S O U R C E") {
            Resource.from(it).shouldBeLeft(ResourceValidationError.MoreThanOneWord)
        }
    }

    "impossible to create Credits resource" - {
        withData("CrEdIts", "Credits", "credits") {
            Resource.from(it).shouldBeLeft(ResourceValidationError.CreditsNameIsReserved)
        }
    }

})
