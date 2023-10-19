package com.abogomazov.merchant.guide.domain.roman

data class Amount(private val value: Int) {
    fun toInt() = value
    fun toBigDecimal() = value.toBigDecimal()
}

fun Int.toAmount() = Amount(this)
