package com.abogomazov.merchant.guide.usecase.translator

import arrow.core.left
import arrow.core.raise.either
import com.abogomazov.merchant.guide.domain.local.LocalNumber
import com.abogomazov.merchant.guide.domain.roman.Amount
import com.abogomazov.merchant.guide.domain.roman.RomanNumber

sealed interface GetTranslationUseCaseError {
    data object TranslationNotFound : GetTranslationUseCaseError
    data object NumberIsNotFollowingRomanNotationRules : GetTranslationUseCaseError
}

class GetTranslationUseCase(
    private val translationProvider: TranslationProvider,
) {
    fun execute(number: LocalNumber) = either<GetTranslationUseCaseError, Amount> {
        val romanDigits = number.digits.map {
            translationProvider.getTranslation(it) ?: return GetTranslationUseCaseError.TranslationNotFound.left()
        }

        return RomanNumber
            .from(romanDigits)
            .mapLeft { GetTranslationUseCaseError.NumberIsNotFollowingRomanNotationRules }
            .map { it.toAmount() }
    }
}
