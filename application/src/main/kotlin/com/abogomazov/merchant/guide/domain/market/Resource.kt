package com.abogomazov.merchant.guide.domain.market

import arrow.core.raise.either
import arrow.core.raise.ensure
import com.abogomazov.merchant.guide.domain.market.ResourceValidationError.*

sealed interface ResourceValidationError {
    data object EmptyString : ResourceValidationError
    data object NonLetters : ResourceValidationError
    data object MoreThanOneWord : ResourceValidationError
    data object CreditsNameIsReserved : ResourceValidationError
}

data class Resource internal constructor(private val value: String) {

    companion object {
        fun from(value: String) = either {
            ensure(value.isNotBlank()) { EmptyString }
            ensure(value.singleWord()) { MoreThanOneWord }
            ensure(value.containsOnlyLetters()) { NonLetters }
            ensure(value.isNotCredits()) { CreditsNameIsReserved }

            Resource(value)
        }
    }

    override fun toString() = value

}

private fun String.isNotCredits() = this.lowercase() != "credits"
private fun String.singleWord() = this.split("\\s+".toRegex()).size == 1
private fun String.containsOnlyLetters() = this.all { it.isLetter() }
