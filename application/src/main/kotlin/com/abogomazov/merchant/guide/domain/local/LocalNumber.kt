package com.abogomazov.merchant.guide.domain.local

import arrow.core.raise.either
import arrow.core.raise.ensure

sealed interface LocalNumberValidationError {
    data object NoDigits : LocalNumberValidationError
}

data class LocalNumber internal constructor(val digits: List<LocalDigit>) {

    companion object {
        fun from(digits: List<LocalDigit>) = either {
            ensure(digits.isNotEmpty()) { LocalNumberValidationError.NoDigits }

            LocalNumber(digits)
        }
    }

    override fun toString() = digits.joinToString(" ")
}
