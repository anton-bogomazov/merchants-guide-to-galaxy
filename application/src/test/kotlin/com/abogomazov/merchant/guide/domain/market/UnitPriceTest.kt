package com.abogomazov.merchant.guide.domain.market

import com.abogomazov.merchant.guide.domain.amount
import com.abogomazov.merchant.guide.domain.bigDec
import com.abogomazov.merchant.guide.domain.credits
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class UnitPriceTest : FreeSpec({

    // TODO MATH! Do a proper testing

    "unit price is a fractional value of 1 unit of resource" {
        UnitPrice.calculate(credits(87), amount(17))
            .toBigDecimal() shouldBe bigDec(5.117647059)
    }
})
