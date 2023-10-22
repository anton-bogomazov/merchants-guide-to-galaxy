package com.abogomazov.merchant.guide.usecase.common

import arrow.core.Either
import arrow.core.left
import com.abogomazov.merchant.guide.domain.local.LocalDigit
import com.abogomazov.merchant.guide.domain.local.LocalNumber
import com.abogomazov.merchant.guide.domain.roman.Amount
import com.abogomazov.merchant.guide.domain.roman.RomanNumber
import org.slf4j.LoggerFactory

sealed interface LocalNumberEvaluationError {
    data class TranslationNotFound(val digit: LocalDigit) : LocalNumberEvaluationError
    data object RomanNotationRulesViolated : LocalNumberEvaluationError
}

class LocalNumberEvaluator(
    private val translationProvider: TranslationProvider,
) {
    fun evaluate(number: LocalNumber): Either<LocalNumberEvaluationError, Amount> {
        logger.info("Evaluating $number")
        return number.toLocalDigits().map { localDigit ->
            val romanNumber = translationProvider.getTranslation(localDigit)
            if (romanNumber == null) {
                logger.error("Translation not found for $localDigit")
                return LocalNumberEvaluationError.TranslationNotFound(localDigit).left()
            }
            romanNumber
        }.let { romanDigits ->
            RomanNumber.from(romanDigits)
                .mapLeft {
                    logger.error("$romanDigits violates roman notation rules: $it")
                    LocalNumberEvaluationError.RomanNotationRulesViolated
                }
                .map {
                    val amount = it.toAmount()
                    logger.info("$number successfully evaluated, result=$amount")
                    amount
                }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(LocalNumberEvaluator::class.java)
    }
}
