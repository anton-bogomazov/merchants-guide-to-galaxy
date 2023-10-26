package com.abogomazov.merchant.guide.usecase.translator

import arrow.core.Either
import com.abogomazov.merchant.guide.domain.galaxy.GalaxyNumeral
import com.abogomazov.merchant.guide.domain.galaxy.GalaxyNumber
import com.abogomazov.merchant.guide.domain.roman.Amount
import com.abogomazov.merchant.guide.usecase.common.GalaxyNumberEvaluationError
import com.abogomazov.merchant.guide.usecase.common.GalaxyNumberEvaluator
import org.slf4j.LoggerFactory

sealed interface GetTranslationError {
    data class TranslationNotFound(val digit: GalaxyNumeral) : GetTranslationError
    data object RomanNotationRulesViolated : GetTranslationError
}

class GetTranslationUseCase(
    private val evaluator: GalaxyNumberEvaluator,
) {
    fun execute(number: GalaxyNumber): Either<GetTranslationError, Amount> {
        logger.info("Getting translation for GalaxyNumber=$number")
        return evaluator.evaluate(number)
            .mapLeft { it.toError() }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(GetTranslationUseCase::class.java)
    }
}

fun GalaxyNumberEvaluationError.toError() =
    when (this) {
        is GalaxyNumberEvaluationError.TranslationNotFound ->
            GetTranslationError.TranslationNotFound(this.digit)
        GalaxyNumberEvaluationError.RomanNotationRulesViolated ->
            GetTranslationError.RomanNotationRulesViolated
    }
