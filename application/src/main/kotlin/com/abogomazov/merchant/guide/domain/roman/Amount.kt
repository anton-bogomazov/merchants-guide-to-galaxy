package com.abogomazov.merchant.guide.domain.roman

import arrow.core.raise.either
import arrow.core.raise.ensure

sealed interface AmountValidationError {
    data object NegativeValue : AmountValidationError
}

data class Amount internal constructor(private val value: Int) {

    companion object {
        fun from(value: Int) = either {
            ensure(value >= 0) { AmountValidationError.NegativeValue }
        }
    }

    fun toInt() = value
    fun toBigDecimal() = value.toBigDecimal()
}
