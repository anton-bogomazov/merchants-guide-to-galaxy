package com.abogomazov.merchant.guide.usecase.common

import arrow.core.left
import arrow.core.raise.either
import com.abogomazov.merchant.guide.domain.local.LocalNumber
import com.abogomazov.merchant.guide.domain.roman.Amount
import com.abogomazov.merchant.guide.domain.roman.RomanNumber

sealed interface LocalNumberEvaluatorError {
    data object TranslationNotFound : LocalNumberEvaluatorError
    data object NumberIsNotFollowingRomanNotationRules : LocalNumberEvaluatorError
}

class LocalNumberEvaluator(
    private val translationProvider: TranslationProvider,
) {
    fun evaluate(number: LocalNumber) = either<LocalNumberEvaluatorError, Amount> {
        val romanDigits = number.digits.map {
            translationProvider.getTranslation(it) ?: return LocalNumberEvaluatorError.TranslationNotFound.left()
        }

        return RomanNumber
            .from(romanDigits)
            .mapLeft { LocalNumberEvaluatorError.NumberIsNotFollowingRomanNotationRules }
            .map { it.toAmount() }
    }
}
