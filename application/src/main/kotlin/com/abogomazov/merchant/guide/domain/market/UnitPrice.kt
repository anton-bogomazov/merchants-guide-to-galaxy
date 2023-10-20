package com.abogomazov.merchant.guide.domain.market

import com.abogomazov.merchant.guide.domain.roman.Amount
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

data class UnitPrice internal constructor(private val value: BigDecimal) {

    companion object {
        fun of(total: Credits, quantity: Amount) = UnitPrice(
            total.toBigDecimal().divide(quantity.toBigDecimal(), roundDownContext())
        )
    }

    fun toBigDecimal() = value
}

private fun roundDownContext() = MathContext(10, RoundingMode.HALF_DOWN)
