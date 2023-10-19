package com.abogomazov.merchant.guide.domain.market

import com.abogomazov.merchant.guide.domain.roman.Amount
import java.math.BigInteger
import java.math.MathContext
import java.math.RoundingMode

data class Credits internal constructor(private val value: BigInteger) {

    companion object {
        fun total(quantity: Amount, price: UnitPrice) = Credits(
            price.toBigDecimal().multiply(quantity.toBigDecimal())
                .round(roundDownContext()).toBigInteger()
        )
    }

    fun toBigInteger() = value
    fun toBigDecimal() = value.toBigDecimal()
}

private fun roundDownContext() = MathContext(0, RoundingMode.DOWN)
