package com.abogomazov.merchant.guide.usecase.common

import arrow.core.Either
import arrow.core.left
import com.abogomazov.merchant.guide.domain.local.LocalNumber
import com.abogomazov.merchant.guide.domain.roman.Amount
import com.abogomazov.merchant.guide.domain.roman.RomanNumber


sealed interface LocalNumberEvaluationError {
    data object TranslationNotFound : LocalNumberEvaluationError
    data object RomanNotationRulesViolated : LocalNumberEvaluationError
}

class LocalNumberEvaluator(
    private val translationProvider: TranslationProvider,
) {
    fun evaluate(number: LocalNumber): Either<LocalNumberEvaluationError, Amount> =
        number.digits.map { localDigit ->
            translationProvider.getTranslation(localDigit)
                ?: return LocalNumberEvaluationError.TranslationNotFound.left()
        }.let { romanDigits ->
            RomanNumber.from(romanDigits)
                .mapLeft { LocalNumberEvaluationError.RomanNotationRulesViolated }
                .map { it.toAmount() }
        }
}
