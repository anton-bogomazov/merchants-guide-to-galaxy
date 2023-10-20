package com.abogomazov.merchant.guide.domain.market

import com.abogomazov.merchant.guide.domain.amount
import com.abogomazov.merchant.guide.domain.bigInt
import com.abogomazov.merchant.guide.domain.price
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class CreditsTest : FreeSpec({

    // TODO MATH! Do a proper testing

    "total credits is a product of amount and unit price rounded down" {
        Credits.total(
            amount(17), price(3.8576f)
        ).toBigInteger() shouldBe bigInt(65)
    }
})
