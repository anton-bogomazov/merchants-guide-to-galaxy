package com.abogomazov.merchant.guide.domain.local

import arrow.core.raise.either
import arrow.core.raise.ensure
import com.abogomazov.merchant.guide.domain.containsOnlyLetters
import com.abogomazov.merchant.guide.domain.local.LocalDigitValidationError.*
import com.abogomazov.merchant.guide.domain.singleWord


sealed interface LocalDigitValidationError {
    data object EmptyString : LocalDigitValidationError
    data object NonLetters : LocalDigitValidationError
    data object MoreThanOneWord : LocalDigitValidationError
}

data class LocalDigit internal constructor(private val value: String) {

    companion object {
        fun of(value: String) = either {
            ensure(value.isNotBlank()) { EmptyString }
            ensure(value.singleWord()) { MoreThanOneWord }
            ensure(value.containsOnlyLetters()) { NonLetters }

            LocalDigit(value)
        }
    }

    override fun toString() = value
}
