package com.abogomazov.merchant.guide.usecase.translator

import arrow.core.left
import arrow.core.raise.either
import com.abogomazov.merchant.guide.domain.galaxy.GalaxyNumeral
import com.abogomazov.merchant.guide.domain.roman.RomanDigit
import com.abogomazov.merchant.guide.usecase.common.TranslationProvider
import org.slf4j.LoggerFactory

sealed interface SetTranslationError {
    data class GalaxyNumeralAlreadyAssociated(val romanDigit: RomanDigit) : SetTranslationError
}

class SetTranslationUseCase(
    private val translationProvider: TranslationProvider,
    private val translationPersister: TranslationPersister,
    private val translationRemover: TranslationRemover,
) {
    fun execute(galaxyNumeral: GalaxyNumeral, romanDigit: RomanDigit) = either<SetTranslationError, Unit> {
        logger.info("Associating (GalaxyNumeral=$galaxyNumeral, RomanDigit=$romanDigit)")
        translationProvider.getTranslation(galaxyNumeral)?.let { associatedRomanDigit ->
            if (associatedRomanDigit != romanDigit) {
                logger.error("GalaxyNumeral=$galaxyNumeral already associated with RomanDigit=$romanDigit")
                return SetTranslationError.GalaxyNumeralAlreadyAssociated(associatedRomanDigit).left()
            }
        }
        removePersistedAssociation(romanDigit)
        translationPersister.associate(galaxyNumeral, romanDigit)
        logger.info("Association set (GalaxyNumeral=$galaxyNumeral, RomanDigit=$romanDigit)")
    }

    private fun removePersistedAssociation(romanDigit: RomanDigit) =
        translationProvider.getTranslation(romanDigit)?.let { associatedGalaxyNumeral ->
            translationRemover.remove(associatedGalaxyNumeral, romanDigit)
            logger.info("Association removed (RomanDigit=$romanDigit, GalaxyNumeral=$associatedGalaxyNumeral)")
        }

    companion object {
        private val logger = LoggerFactory.getLogger(SetTranslationUseCase::class.java)
    }
}

fun interface TranslationPersister {
    fun associate(galaxyNumeral: GalaxyNumeral, romanDigit: RomanDigit)
}

fun interface TranslationRemover {
    fun remove(galaxyNumeral: GalaxyNumeral, romanDigit: RomanDigit)
}
