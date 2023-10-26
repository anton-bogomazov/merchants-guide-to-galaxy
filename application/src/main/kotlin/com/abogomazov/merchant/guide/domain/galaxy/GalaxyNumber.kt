package com.abogomazov.merchant.guide.domain.galaxy

import arrow.core.raise.either
import arrow.core.raise.ensure

sealed interface GalaxyNumberValidationError {
    data object NoDigits : GalaxyNumberValidationError
}

data class GalaxyNumber internal constructor(
    private val digits: List<GalaxyNumeral>
) {

    companion object {
        fun from(digits: List<GalaxyNumeral>) = either {
            ensure(digits.isNotEmpty()) { GalaxyNumberValidationError.NoDigits }

            GalaxyNumber(digits)
        }
    }

    fun toGalaxyNumerals() = digits
    override fun toString() = digits.joinToString(" ")
}
