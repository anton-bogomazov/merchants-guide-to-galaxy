package com.abogomazov.merchant.guide.usecase.translator

import arrow.core.raise.either
import arrow.core.raise.ensure
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
    fun execute(romanNumeral: RomanNumeral, galaxyNumeral: GalaxyNumeral) = either<SetTranslationError, Unit> {
        getAssociatedRomanNumeral(galaxyNumeral)?.let {
            ensure(romanNumeral == it) { SetTranslationError.GalaxyNumeralAlreadyAssociated(it) }
        }
        removePersistedAssociationIfExists(romanNumeral)
        translationPersister.associate(galaxyNumeral, romanNumeral)
        logger.info("Association set (GalaxyNumeral=$galaxyNumeral, RomanNumeral=$romanNumeral)")
    }

    private fun getAssociatedRomanNumeral(galaxyNumeral: GalaxyNumeral) =
        translationProvider.getTranslation(galaxyNumeral)

    private fun removePersistedAssociationIfExists(romanNumeral: RomanNumeral) =
        translationProvider.getTranslation(romanNumeral)?.let { associatedGalaxyNumeral ->
            translationRemover.remove(associatedGalaxyNumeral, romanNumeral)
            logger.info("Association removed (RomanNumeral=$romanNumeral, GalaxyNumeral=$associatedGalaxyNumeral)")
        }

    companion object {
        private val logger = LoggerFactory.getLogger(SetTranslationUseCase::class.java)
    }
}
