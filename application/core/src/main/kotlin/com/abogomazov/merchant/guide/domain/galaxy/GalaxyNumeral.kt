package com.abogomazov.merchant.guide.domain.galaxy

import arrow.core.raise.either
import arrow.core.raise.ensure
import com.abogomazov.merchant.guide.domain.containsOnlyLetters
import com.abogomazov.merchant.guide.domain.singleWord

sealed interface GalaxyNumeralValidationError {
    data object EmptyString : GalaxyNumeralValidationError
    data object NonLetters : GalaxyNumeralValidationError
    data object MoreThanOneWord : GalaxyNumeralValidationError
}

data class GalaxyNumeral internal constructor(private val value: String) {

    companion object {
        fun from(value: String) = either {
            ensure(value.isNotBlank()) { GalaxyNumeralValidationError.EmptyString }
            ensure(value.singleWord()) { GalaxyNumeralValidationError.MoreThanOneWord }
            ensure(value.containsOnlyLetters()) { GalaxyNumeralValidationError.NonLetters }

            GalaxyNumeral(value)
        }
    }

    override fun toString() = value
}
