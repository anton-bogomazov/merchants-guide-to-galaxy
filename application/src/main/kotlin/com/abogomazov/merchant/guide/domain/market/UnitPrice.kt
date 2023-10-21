package com.abogomazov.merchant.guide.domain.market

import com.abogomazov.merchant.guide.domain.roman.Amount
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

data class UnitPrice internal constructor(private val value: BigDecimal) {

    companion object {
        fun calculate(total: Credits, quantity: Amount) = UnitPrice(
            total.toBigDecimal().divide(quantity.toBigDecimal(), roundDownContext())
        )

        // TODO Add validation: non-negative
        fun from(value: BigDecimal) = UnitPrice(value)
    }

    fun toBigDecimal() = value
}

private const val TOTAL_DIGITS = 10
private fun roundDownContext() = MathContext(TOTAL_DIGITS, RoundingMode.HALF_DOWN)
