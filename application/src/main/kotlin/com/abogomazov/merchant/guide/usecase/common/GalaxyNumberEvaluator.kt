package com.abogomazov.merchant.guide.usecase.common

import arrow.core.Either
import arrow.core.left
import com.abogomazov.merchant.guide.domain.galaxy.GalaxyNumeral
import com.abogomazov.merchant.guide.domain.galaxy.GalaxyNumber
import com.abogomazov.merchant.guide.domain.roman.Amount
import com.abogomazov.merchant.guide.domain.roman.RomanNumber
import org.slf4j.LoggerFactory

sealed interface GalaxyNumberEvaluationError {
    data class TranslationNotFound(val digit: GalaxyNumeral) : GalaxyNumberEvaluationError
    data object RomanNotationRulesViolated : GalaxyNumberEvaluationError
}

class GalaxyNumberEvaluator(
    private val translationProvider: TranslationProvider,
) {
    fun evaluate(number: GalaxyNumber): Either<GalaxyNumberEvaluationError, Amount> {
        logger.info("Evaluating $number")
        return number.toGalaxyNumerals().map { galaxyNumeral ->
            val romanNumber = translationProvider.getTranslation(galaxyNumeral)
            if (romanNumber == null) {
                logger.error("Translation not found for $galaxyNumeral")
                return GalaxyNumberEvaluationError.TranslationNotFound(galaxyNumeral).left()
            }
            romanNumber
        }.let { romanDigits ->
            RomanNumber.from(romanDigits)
                .mapLeft {
                    logger.error("$romanDigits violates roman notation rules: $it")
                    GalaxyNumberEvaluationError.RomanNotationRulesViolated
                }
                .map {
                    val amount = it.toAmount()
                    logger.info("$number successfully evaluated, result=$amount")
                    amount
                }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(GalaxyNumberEvaluator::class.java)
    }
}
