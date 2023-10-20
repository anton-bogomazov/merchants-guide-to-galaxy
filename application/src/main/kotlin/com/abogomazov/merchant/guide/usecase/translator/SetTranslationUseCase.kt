package com.abogomazov.merchant.guide.usecase.translator

import arrow.core.left
import arrow.core.raise.either
import com.abogomazov.merchant.guide.domain.local.LocalDigit
import com.abogomazov.merchant.guide.domain.roman.RomanDigit
import com.abogomazov.merchant.guide.usecase.common.TranslationProvider

sealed interface SetTranslationError {
    data class LocalDigitAlreadyAssociated(val romanDigit: RomanDigit) : SetTranslationError
}

class SetTranslationUseCase(
    private val translationProvider: TranslationProvider,
    private val translationPersister: TranslationPersister,
    private val translationRemover: TranslationRemover,
) {
    fun execute(localDigit: LocalDigit, romanDigit: RomanDigit) = either<SetTranslationError, Unit> {
        translationProvider.getTranslation(localDigit)?.let { associatedRomanDigit ->
            if (associatedRomanDigit != romanDigit) {
                return SetTranslationError.LocalDigitAlreadyAssociated(associatedRomanDigit).left()
            }
        }
        removePersistedAssociation(romanDigit)
        translationPersister.associate(localDigit, romanDigit)
    }

    private fun removePersistedAssociation(romanDigit: RomanDigit) =
        translationProvider.getTranslation(romanDigit)?.let { associatedLocalDigit ->
            translationRemover.remove(associatedLocalDigit, romanDigit)
        }
}

fun interface TranslationPersister {
    fun associate(localDigit: LocalDigit, romanDigit: RomanDigit)
}

fun interface TranslationRemover {
    fun remove(localDigit: LocalDigit, romanDigit: RomanDigit)
}
