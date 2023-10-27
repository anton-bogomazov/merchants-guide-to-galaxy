package com.abogomazov.merchant.guide.domain.market

import arrow.core.left
import arrow.core.raise.either
import arrow.core.raise.ensure
import com.abogomazov.merchant.guide.domain.roman.Amount
import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext
import java.math.RoundingMode

sealed interface CreditsValidationError {
    data object NegativeValue : CreditsValidationError
    data object NanString : CreditsValidationError
}

data class Credits internal constructor(private val value: BigInteger) {

    companion object {
        fun total(quantity: Amount, price: UnitPrice) =
            Credits(
                price.toBigDecimal()
                    .multiply(quantity.toBigDecimal())
                    .floor()
                    .toBigInteger()
            )

        fun from(value: BigInteger) = either<CreditsValidationError, Credits> {
            ensure(value >= BigInteger.ZERO) { CreditsValidationError.NegativeValue }

            Credits(value)
        }
    }

    fun toBigInteger() = value
    fun toBigDecimal() = value.toBigDecimal()
}

private fun BigDecimal.floor() =
    this.round(MathContext(0, RoundingMode.DOWN))

fun String.toCredit() =
    try {
        Credits.from(BigInteger(this@toCredit))
    } catch (e: NumberFormatException) {
        CreditsValidationError.NanString.left()
    }
