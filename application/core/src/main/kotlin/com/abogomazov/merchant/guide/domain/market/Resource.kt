package com.abogomazov.merchant.guide.domain.market

import arrow.core.raise.either
import arrow.core.raise.ensure
import com.abogomazov.merchant.guide.domain.containsOnlyLetters
import com.abogomazov.merchant.guide.domain.singleWord

sealed interface ResourceValidationError {
    data object EmptyString : ResourceValidationError
    data object NonLetters : ResourceValidationError
    data object MoreThanOneWord : ResourceValidationError
    data object CreditsNameIsReserved : ResourceValidationError
}

data class Resource internal constructor(private val value: String) {

    companion object {
        fun from(value: String) = either {
            ensure(value.isNotBlank()) { ResourceValidationError.EmptyString }
            ensure(value.singleWord()) { ResourceValidationError.MoreThanOneWord }
            ensure(value.containsOnlyLetters()) { ResourceValidationError.NonLetters }
            ensure(value.isNotCredits()) { ResourceValidationError.CreditsNameIsReserved }

            Resource(value)
        }
    }

    override fun toString() = value
}

private fun String.isNotCredits() = this.lowercase() != "credits"

fun String.toResource() = Resource.from(this)
