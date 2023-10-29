package com.abogomazov.merchant.guide.domain.roman

enum class RomanNumeral(val value: Int) {
    I(value = 1),
    V(value = 5),
    X(value = 10),
    L(value = 50),
    C(value = 100),
    D(value = 500),
    M(value = 1000)
}

// TODO catch conversion from invalid string
fun String.toRomanNumeral() = RomanNumeral.valueOf(this)
