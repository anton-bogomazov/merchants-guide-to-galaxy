package com.abogomazov.merchant.guide.domain.roman

import arrow.core.left
import arrow.core.right

enum class RomanNumeral(val value: Int) {
    I(value = 1),
    V(value = 5),
    X(value = 10),
    L(value = 50),
    C(value = 100),
    D(value = 500),
    M(value = 1000)
}

@Suppress("SwallowedException")
fun String.toRomanNumeral() =
    try {
        RomanNumeral.valueOf(this.uppercase()).right()
    } catch (e: IllegalArgumentException) {
        RomanNumeralConstructionError(this).left()
    }

data class RomanNumeralConstructionError(val invalidNumeral: String)
