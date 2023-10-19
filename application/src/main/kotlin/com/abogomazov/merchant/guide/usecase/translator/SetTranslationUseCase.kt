package com.abogomazov.merchant.guide.usecase.translator

import arrow.core.left
import arrow.core.raise.either
import arrow.core.right
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
        val associatedRomanDigit = translationProvider.getTranslation(localDigit)
        if (associatedRomanDigit != null) {
            // ignore trying to overwrite with the same value
            if (associatedRomanDigit == romanDigit) return Unit.right()
            // impossible to associate with non-unique LocalDigit
            return SetTranslationUseCaseError.LocalDigitAlreadyAssociatedWithRoman.left()
        }

        val associatedLocalDigit = translationProvider.getTranslation(romanDigit)
        if (associatedLocalDigit != null) {
            // remove old association
            translationRemover.remove(associatedLocalDigit, romanDigit)
        }

        translationPersister.associate(localDigit, romanDigit)
    }
}

fun interface TranslationPersister {
    fun associate(localDigit: LocalDigit, romanDigit: RomanDigit)
}

fun interface TranslationRemover {
    fun remove(localDigit: LocalDigit, romanDigit: RomanDigit)
}
