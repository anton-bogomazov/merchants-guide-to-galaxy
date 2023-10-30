package com.abogomazov.merchant.guide.domain.market

import com.abogomazov.merchant.guide.domain.amount
import com.abogomazov.merchant.guide.domain.credits
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.bigdecimal.shouldBeGreaterThan
import io.kotest.matchers.bigdecimal.shouldBeLessThan
import io.kotest.matchers.bigdecimal.shouldHavePrecision
import io.kotest.matchers.comparables.shouldBeGreaterThanOrEqualTo
import io.kotest.matchers.comparables.shouldBeLessThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bigDecimal
import io.kotest.property.arbitrary.bigInt
import io.kotest.property.assume
import io.kotest.property.checkAll
import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext
import java.math.RoundingMode

class UnitPriceTest : FreeSpec({

    "unit price is the result of dividing the cost of a resource by its quantity" {
        checkAll(Arb.bigInt(256), Arb.amount()) { creditsBigInt, amount ->
            assume { creditsBigInt shouldNotBe BigInteger.ZERO }

            val result = UnitPrice.calculate(credits(creditsBigInt), amount)
            result.toBigDecimal() shouldBeGreaterThan BigDecimal.ZERO
            result.toBigDecimal() shouldHavePrecision 30
            result.toBigDecimal() shouldBeLessThan creditsBigInt.toBigDecimal()

            val expected = creditsBigInt.toBigDecimal()
                .divide(amount.toBigDecimal(), MathContext(30, RoundingMode.HALF_DOWN))

            result.toBigDecimal() shouldBe expected
        }
    }

    "unit price represents any positive biginteger or zero" {
        checkAll(Arb.bigDecimal()) {
            assume { it shouldBeGreaterThanOrEqualTo BigDecimal.ZERO }

            UnitPrice.from(it).shouldBeRight()
        }
    }

    "unit price cannot represent negative biginteger" {
        checkAll(Arb.bigDecimal()) {
            assume { it shouldBeLessThan BigDecimal.ZERO }

            UnitPrice.from(it)
                .shouldBeLeft(UnitPriceValidationError.NegativeValue)
        }
    }

    "convert nan string to unit price" {
        "thousand".toUnitPrice().shouldBeLeft(UnitPriceValidationError.NanString)
    }
})
