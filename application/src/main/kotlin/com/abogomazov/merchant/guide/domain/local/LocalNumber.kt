package com.abogomazov.merchant.guide.domain.local

import arrow.core.raise.either
import arrow.core.raise.ensure
import com.abogomazov.merchant.guide.domain.local.LocalNumberValidationError.*


sealed interface LocalNumberValidationError {
    data object NoDigits : LocalNumberValidationError
}

data class LocalNumber internal constructor(val digits: List<LocalDigit>) {

    companion object {
        fun from(digits: List<LocalDigit>) = either {
            ensure(digits.isNotEmpty()) { NoDigits }
        }
    }

    override fun toString() = digits.joinToString(" ")
}
