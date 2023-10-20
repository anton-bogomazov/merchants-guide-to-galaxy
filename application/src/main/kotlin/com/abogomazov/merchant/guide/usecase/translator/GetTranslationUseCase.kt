package com.abogomazov.merchant.guide.usecase.translator

import arrow.core.Either
import com.abogomazov.merchant.guide.domain.local.LocalDigit
import com.abogomazov.merchant.guide.domain.local.LocalNumber
import com.abogomazov.merchant.guide.domain.roman.Amount
import com.abogomazov.merchant.guide.usecase.common.LocalNumberEvaluationError
import com.abogomazov.merchant.guide.usecase.common.LocalNumberEvaluator
import org.slf4j.LoggerFactory

sealed interface GetTranslationError {
    data class TranslationNotFound(val digit: LocalDigit) : GetTranslationError
    data object RomanNotationRulesViolated : GetTranslationError
}

class GetTranslationUseCase(
    private val evaluator: LocalNumberEvaluator,
) {
    fun execute(number: LocalNumber): Either<GetTranslationError, Amount> {
        logger.info("Getting translation for LocalNumber=$number")
        return evaluator.evaluate(number)
            .mapLeft { it.toError() }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(GetTranslationUseCase::class.java)
    }
}

fun LocalNumberEvaluationError.toError() =
    when (this) {
        is LocalNumberEvaluationError.TranslationNotFound ->
            GetTranslationError.TranslationNotFound(this.digit)
        LocalNumberEvaluationError.RomanNotationRulesViolated ->
            GetTranslationError.RomanNotationRulesViolated
    }
