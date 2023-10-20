package com.abogomazov.merchant.guide.usecase.translator

import arrow.core.raise.either
import arrow.core.raise.ensure
import com.abogomazov.merchant.guide.domain.local.LocalDigit
import com.abogomazov.merchant.guide.domain.roman.RomanDigit
import com.abogomazov.merchant.guide.usecase.common.TranslationProvider

sealed interface SetTranslationUseCaseError {
    data object LocalDigitAlreadyAssociatedWithRoman : SetTranslationUseCaseError
}

class SetTranslationUseCase(
    private val translationProvider: TranslationProvider,
    private val translationPersister: TranslationPersister,
    private val translationRemover: TranslationRemover,
) {
    fun execute(localDigit: LocalDigit, romanDigit: RomanDigit) = either<SetTranslationUseCaseError, Unit> {
        ensure(!localDigit.isAlreadyAssociated(romanDigit)) { SetTranslationUseCaseError.LocalDigitAlreadyAssociatedWithRoman }

        removePersistedAssociation(romanDigit)
        translationPersister.associate(localDigit, romanDigit)
    }

    private fun removePersistedAssociation(romanDigit: RomanDigit) =
        translationProvider.getTranslation(romanDigit)?.let { associatedLocalDigit ->
            translationRemover.remove(associatedLocalDigit, romanDigit)
        }

    private fun LocalDigit.isAlreadyAssociated(romanDigit: RomanDigit): Boolean {
        val associatedRomanDigit = translationProvider.getTranslation(this) ?: return false
        // allowed to overwrite association with the same values
        val isSameRomanDigit = associatedRomanDigit == romanDigit
        return !isSameRomanDigit
    }
}

fun interface TranslationPersister {
    fun associate(localDigit: LocalDigit, romanDigit: RomanDigit)
}

fun interface TranslationRemover {
    fun remove(localDigit: LocalDigit, romanDigit: RomanDigit)
}
