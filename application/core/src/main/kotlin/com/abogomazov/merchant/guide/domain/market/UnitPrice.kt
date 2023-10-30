package com.abogomazov.merchant.guide.domain.market

import arrow.core.flatMap
import arrow.core.left
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.right
import com.abogomazov.merchant.guide.domain.roman.Amount
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

sealed interface UnitPriceValidationError {
    data object NegativeValue : UnitPriceValidationError
    data object NanString : UnitPriceValidationError
}

data class UnitPrice internal constructor(private val value: BigDecimal) {

    companion object {
        fun calculate(total: Credits, quantity: Amount) = UnitPrice(
            total.toBigDecimal().divide(quantity.toBigDecimal(), roundDownContext())
        )

        fun from(value: BigDecimal) = either {
            ensure(value >= BigDecimal.ZERO) { UnitPriceValidationError.NegativeValue }

            UnitPrice(value)
        }
    }

    fun toBigDecimal() = value
}

private const val TOTAL_DIGITS = 30
private fun roundDownContext() = MathContext(TOTAL_DIGITS, RoundingMode.HALF_DOWN)

fun String.toUnitPrice() =
    try {
        BigDecimal(this).right()
    } catch (e: NumberFormatException) {
        UnitPriceValidationError.NanString.left()
    }.flatMap {
        UnitPrice.from(it)
    }
