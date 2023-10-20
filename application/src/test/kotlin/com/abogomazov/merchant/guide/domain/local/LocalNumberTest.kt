package com.abogomazov.merchant.guide.domain.local

import com.abogomazov.merchant.guide.domain.five
import com.abogomazov.merchant.guide.domain.one
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FreeSpec
import io.kotest.datatest.withData

class LocalNumberTest : FreeSpec({
    "local number could be created with a sequence of local digits" - {
        withData(
            listOf(one()),
            listOf(one(), five()),
            List(1000) { one() },
        ) {
            LocalNumber.from(it).shouldBeRight()
        }
    }

    "impossible to create number from empty list of digits" {
        LocalNumber.from(emptyList()).shouldBeLeft(LocalNumberValidationError.NoDigits)
    }
})
