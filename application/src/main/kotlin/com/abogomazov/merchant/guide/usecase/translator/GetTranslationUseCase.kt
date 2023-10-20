package com.abogomazov.merchant.guide.usecase.translator

import com.abogomazov.merchant.guide.domain.local.LocalNumber
import com.abogomazov.merchant.guide.usecase.common.LocalNumberEvaluator
import com.abogomazov.merchant.guide.usecase.common.LocalNumberEvaluationError

sealed interface GetTranslationError {
    data object TranslationNotFound : GetTranslationError
    data object RomanNotationRulesViolated : GetTranslationError
}

class GetTranslationUseCase(
    private val evaluator: LocalNumberEvaluator,
) {
    fun execute(number: LocalNumber) =
        evaluator.evaluate(number)
            .mapLeft { it.toError() }
}

fun LocalNumberEvaluationError.toError() =
    when (this) {
        LocalNumberEvaluationError.TranslationNotFound ->
            GetTranslationError.TranslationNotFound
        LocalNumberEvaluationError.RomanNotationRulesViolated ->
            GetTranslationError.RomanNotationRulesViolated
    }
