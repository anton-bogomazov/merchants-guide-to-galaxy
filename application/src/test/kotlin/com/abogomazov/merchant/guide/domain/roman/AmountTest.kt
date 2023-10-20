package com.abogomazov.merchant.guide.domain.roman

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FreeSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.negativeInt
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.checkAll

class AmountTest : FreeSpec({

    "Amount is any positive int" {
        checkAll(Arb.positiveInt()) {
            Amount.from(it).shouldBeRight()
        }
    }

    "Amount could be zero" {
        Amount.from(0).shouldBeRight()
    }

    "Amount cannot represent negative value" {
        checkAll(Arb.negativeInt()) {
            Amount.from(it).shouldBeLeft(AmountValidationError.NegativeValue)
        }
    }

})
