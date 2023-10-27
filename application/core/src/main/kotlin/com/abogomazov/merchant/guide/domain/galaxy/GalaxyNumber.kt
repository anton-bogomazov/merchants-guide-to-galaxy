package com.abogomazov.merchant.guide.domain.galaxy

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.raise.either
import arrow.core.raise.ensure

sealed interface GalaxyNumberValidationError {
    data object NoDigits : GalaxyNumberValidationError
    data object InvalidGalaxyNumeral : GalaxyNumberValidationError
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

fun String.toGalaxyNumber(): Either<GalaxyNumberValidationError, GalaxyNumber> =
    this.sanitized().split(" ")
        .map { GalaxyNumeral.from(it) }
        .let { l -> either { l.bindAll() } }
        .mapLeft { GalaxyNumberValidationError.InvalidGalaxyNumeral }
        .flatMap { digits ->
            GalaxyNumber.from(digits)
        }

private fun String.sanitized() = this.replace("\\s+".toRegex(), " ").trim()
