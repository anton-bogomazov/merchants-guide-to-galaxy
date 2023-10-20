package com.abogomazov.merchant.guide.usecase.common

import arrow.core.Either
import arrow.core.left
import com.abogomazov.merchant.guide.domain.local.LocalNumber
import com.abogomazov.merchant.guide.domain.roman.Amount
import com.abogomazov.merchant.guide.domain.roman.RomanNumber
import com.abogomazov.merchant.guide.usecase.common.LocalNumberEvaluatorError.*


sealed interface LocalNumberEvaluatorError {
    data object TranslationNotFound : LocalNumberEvaluatorError
    data object NumberIsNotFollowingRomanNotationRules : LocalNumberEvaluatorError
}

class LocalNumberEvaluator(
    private val translationProvider: TranslationProvider,
) {
    fun evaluate(number: LocalNumber): Either<LocalNumberEvaluatorError, Amount> =
        number.digits.map { localDigit ->
            translationProvider.getTranslation(localDigit)
                ?: return TranslationNotFound.left()
        }.let { romanDigits ->
            RomanNumber.from(romanDigits)
                .mapLeft { NumberIsNotFollowingRomanNotationRules }
                .map { it.toAmount() }
        }
}
