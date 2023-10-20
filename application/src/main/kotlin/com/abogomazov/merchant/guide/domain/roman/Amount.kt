package com.abogomazov.merchant.guide.domain.roman

// TODO defense from negatives
data class Amount(private val value: Int) {
    fun toInt() = value
    fun toBigDecimal() = value.toBigDecimal()
}

fun Int.toAmount() = Amount(this)
