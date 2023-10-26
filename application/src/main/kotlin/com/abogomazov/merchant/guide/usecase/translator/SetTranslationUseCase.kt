package com.abogomazov.merchant.guide.usecase.translator

import arrow.core.left
import arrow.core.raise.either
import com.abogomazov.merchant.guide.domain.galaxy.GalaxyNumeral
import com.abogomazov.merchant.guide.domain.roman.RomanNumeral
import com.abogomazov.merchant.guide.usecase.common.TranslationProvider
import org.slf4j.LoggerFactory

sealed interface SetTranslationError {
    data class GalaxyNumeralAlreadyAssociated(val romanNumeral: RomanNumeral) : SetTranslationError
}

class SetTranslationUseCase(
    private val translationProvider: TranslationProvider,
    private val translationPersister: TranslationPersister,
    private val translationRemover: TranslationRemover,
) {
    fun execute(galaxyNumeral: GalaxyNumeral, romanNumeral: RomanNumeral) = either<SetTranslationError, Unit> {
        logger.info("Associating (GalaxyNumeral=$galaxyNumeral, RomanNumeral=$romanNumeral)")
        translationProvider.getTranslation(galaxyNumeral)?.let { associatedRomanNumeral ->
            if (associatedRomanNumeral != romanNumeral) {
                logger.error("GalaxyNumeral=$galaxyNumeral already associated with RomanNumeral=$romanNumeral")
                return SetTranslationError.GalaxyNumeralAlreadyAssociated(associatedRomanNumeral).left()
            }
        }
        removePersistedAssociation(romanNumeral)
        translationPersister.associate(galaxyNumeral, romanNumeral)
        logger.info("Association set (GalaxyNumeral=$galaxyNumeral, RomanNumeral=$romanNumeral)")
    }

    private fun removePersistedAssociation(romanNumeral: RomanNumeral) =
        translationProvider.getTranslation(romanNumeral)?.let { associatedGalaxyNumeral ->
            translationRemover.remove(associatedGalaxyNumeral, romanNumeral)
            logger.info("Association removed (RomanNumeral=$romanNumeral, GalaxyNumeral=$associatedGalaxyNumeral)")
        }

    companion object {
        private val logger = LoggerFactory.getLogger(SetTranslationUseCase::class.java)
    }
}

fun interface TranslationPersister {
    fun associate(galaxyNumeral: GalaxyNumeral, romanNumeral: RomanNumeral)
}

fun interface TranslationRemover {
    fun remove(galaxyNumeral: GalaxyNumeral, romanNumeral: RomanNumeral)
}
