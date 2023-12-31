package com.abogomazov.merchant.guide.domain.market

import com.abogomazov.merchant.guide.domain.amount
import com.abogomazov.merchant.guide.domain.credits
import com.abogomazov.merchant.guide.domain.dropFractional
import com.abogomazov.merchant.guide.domain.price
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.bigdecimal.shouldBeGreaterThan
import io.kotest.matchers.comparables.shouldBeGreaterThanOrEqualTo
import io.kotest.matchers.comparables.shouldBeLessThan
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bigInt
import io.kotest.property.arbitrary.next
import io.kotest.property.assume
import io.kotest.property.checkAll
import java.math.BigDecimal
import java.math.BigInteger

class CreditsTest : FreeSpec({
    "total credits is a product of amount and unit price rounded down" {
        checkAll(Arb.amount(), Arb.price()) { amount, price ->
            val result = Credits.total(amount, price)

            result.toBigDecimal() shouldBeGreaterThan BigDecimal.ZERO
            result.toBigDecimal() shouldBeGreaterThan amount.toBigDecimal()
            result.toBigDecimal() shouldBeGreaterThan price.toBigDecimal()

            val expected = amount.toBigDecimal() * price.toBigDecimal()
            result.toBigDecimal() shouldBe expected.dropFractional()
        }
    }

    "total credits could be 0 if price of resource or amount is 0" {
        Credits.total(Arb.amount().next(), price(0.0)) shouldBe credits(0)
        Credits.total(amount(0), Arb.price().next()) shouldBe credits(0)
    }

    "credits represents any positive biginteger or zero" {
        checkAll(Arb.bigInt(256)) {
            assume { it shouldBeGreaterThanOrEqualTo BigInteger.ZERO }

            Credits.from(it).shouldBeRight()
        }
    }

    "credits cannot represent negative biginteger" {
        checkAll(Arb.bigInt(256)) {
            assume { it.negate() shouldBeLessThan BigInteger.ZERO }

            Credits.from(it.negate()).shouldBeLeft(CreditsValidationError.NegativeValue)
        }
    }

    "credits can be constructed from primitive String" {
        "123456789876543234567897654323456789876543".toCredit()
            .shouldBeRight(credits(BigInteger("123456789876543234567897654323456789876543")))
    }

    "credits cannot be constructed from String representing negative number" {
        "-6453".toCredit()
            .shouldBeLeft(CreditsValidationError.NegativeValue)
    }

    "credits cannot be constructed from not a number String" {
        "hello".toCredit()
            .shouldBeLeft(CreditsValidationError.NanString)
    }
})
