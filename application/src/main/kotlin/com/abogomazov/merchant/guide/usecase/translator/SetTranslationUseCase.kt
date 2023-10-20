package com.abogomazov.merchant.guide.usecase.translator

import arrow.core.left
import arrow.core.raise.either
import com.abogomazov.merchant.guide.domain.local.LocalDigit
import com.abogomazov.merchant.guide.domain.roman.RomanDigit
import com.abogomazov.merchant.guide.usecase.common.TranslationProvider
import org.slf4j.LoggerFactory

sealed interface SetTranslationError {
    data class LocalDigitAlreadyAssociated(val romanDigit: RomanDigit) : SetTranslationError
}

class SetTranslationUseCase(
    private val translationProvider: TranslationProvider,
    private val translationPersister: TranslationPersister,
    private val translationRemover: TranslationRemover,
) {
    fun execute(localDigit: LocalDigit, romanDigit: RomanDigit) = either<SetTranslationError, Unit> {
        logger.info("Associating (LocalDigit=$localDigit, RomanDigit=$romanDigit)")
        translationProvider.getTranslation(localDigit)?.let { associatedRomanDigit ->
            if (associatedRomanDigit != romanDigit) {
                logger.error("LocalDigit=$localDigit already associated with RomanDigit=$romanDigit")
                return SetTranslationError.LocalDigitAlreadyAssociated(associatedRomanDigit).left()
            }
        }
        removePersistedAssociation(romanDigit)
        translationPersister.associate(localDigit, romanDigit)
        logger.info("Association set (LocalDigit=$localDigit, RomanDigit=$romanDigit)")
    }

    private fun removePersistedAssociation(romanDigit: RomanDigit) =
        translationProvider.getTranslation(romanDigit)?.let { associatedLocalDigit ->
            translationRemover.remove(associatedLocalDigit, romanDigit)
            logger.info("Association removed (RomanDigit=$romanDigit, LocalDigit=$associatedLocalDigit)")
        }

    companion object {
        private val logger = LoggerFactory.getLogger(SetTranslationUseCase::class.java)
    }
}

fun interface TranslationPersister {
    fun associate(localDigit: LocalDigit, romanDigit: RomanDigit)
}

fun interface TranslationRemover {
    fun remove(localDigit: LocalDigit, romanDigit: RomanDigit)
}
