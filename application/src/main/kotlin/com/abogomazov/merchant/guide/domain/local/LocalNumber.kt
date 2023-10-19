package com.abogomazov.merchant.guide.domain.local

data class LocalNumber(val digits: List<LocalDigit>) {
    override fun toString() = digits.joinToString(" ")
}
