package com.abogomazov.merchant.guide.usecase.common

import arrow.core.flatMap
import arrow.core.left
import arrow.core.raise.either
import arrow.core.right
import com.abogomazov.merchant.guide.domain.galaxy.GalaxyNumber
import com.abogomazov.merchant.guide.domain.galaxy.GalaxyNumeral
import com.abogomazov.merchant.guide.domain.roman.RomanNumber
import com.abogomazov.merchant.guide.domain.roman.RomanNumeral
import org.slf4j.LoggerFactory

sealed interface GalaxyNumberEvaluationError {
    data class TranslationNotFound(val digit: GalaxyNumeral) : GalaxyNumberEvaluationError
    data object RomanNotationRulesViolated : GalaxyNumberEvaluationError
}

class GalaxyNumberEvaluator(
    private val translationProvider: TranslationProvider,
) {
    fun amountOf(number: GalaxyNumber) =
        number.toGalaxyNumerals()
            .toRomanNumerals()
            .flatMap { it.toRomanNumber() }
            .map { romanNumber ->
                romanNumber.toAmount()
                    .also { logger.info("$number successfully evaluated, result=$it") }
            }

    private fun List<GalaxyNumeral>.toRomanNumerals() =
        this.map { galaxyNumeral ->
            translationProvider.getTranslation(galaxyNumeral)?.right()
                ?: GalaxyNumberEvaluationError.TranslationNotFound(galaxyNumeral).left()
                    .also { logger.error("Translation not found for $galaxyNumeral") }
        }.let { l -> either { l.bindAll() } }

    private fun List<RomanNumeral>.toRomanNumber() =
        RomanNumber.from(this)
            .mapLeft {
                logger.error("$this violates roman notation rules: $it")
                GalaxyNumberEvaluationError.RomanNotationRulesViolated
            }

    companion object {
        private val logger = LoggerFactory.getLogger(GalaxyNumberEvaluator::class.java)
    }
}
