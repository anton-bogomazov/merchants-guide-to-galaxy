package com.abogomazov.merchant.guide.usecase.translator

import com.abogomazov.merchant.guide.domain.local.LocalNumber
import com.abogomazov.merchant.guide.usecase.common.LocalNumberEvaluator
import com.abogomazov.merchant.guide.usecase.common.LocalNumberEvaluatorError

sealed interface GetTranslationUseCaseError {
    data object TranslationNotFound : GetTranslationUseCaseError
    data object NumberIsNotFollowingRomanNotationRules : GetTranslationUseCaseError
}

class GetTranslationUseCase(
    private val evaluator: LocalNumberEvaluator,
) {
    fun execute(number: LocalNumber) =
        evaluator.evaluate(number)
            .mapLeft { it.toError() }
}

fun LocalNumberEvaluatorError.toError() =
    when (this) {
        LocalNumberEvaluatorError.TranslationNotFound -> GetTranslationUseCaseError.TranslationNotFound
        LocalNumberEvaluatorError.NumberIsNotFollowingRomanNotationRules ->
            GetTranslationUseCaseError.NumberIsNotFollowingRomanNotationRules
    }
