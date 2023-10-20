package com.abogomazov.merchant.guide.domain.local

import arrow.core.raise.either
import arrow.core.raise.ensure
import com.abogomazov.merchant.guide.domain.containsOnlyLetters
import com.abogomazov.merchant.guide.domain.singleWord

sealed interface LocalDigitValidationError {
    data object EmptyString : LocalDigitValidationError
    data object NonLetters : LocalDigitValidationError
    data object MoreThanOneWord : LocalDigitValidationError
}

data class LocalDigit internal constructor(private val value: String) {

    companion object {
        fun from(value: String) = either {
            ensure(value.isNotBlank()) { LocalDigitValidationError.EmptyString }
            ensure(value.singleWord()) { LocalDigitValidationError.MoreThanOneWord }
            ensure(value.containsOnlyLetters()) { LocalDigitValidationError.NonLetters }

            LocalDigit(value)
        }
    }

    override fun toString() = value
}
