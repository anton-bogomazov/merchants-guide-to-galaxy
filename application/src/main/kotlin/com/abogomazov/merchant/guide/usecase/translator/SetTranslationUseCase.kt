package com.abogomazov.merchant.guide.usecase.translator

import arrow.core.left
import arrow.core.raise.either
import arrow.core.right
import com.abogomazov.merchant.guide.domain.local.LocalDigit
import com.abogomazov.merchant.guide.domain.roman.RomanDigit

sealed interface SetTranslationUseCaseError {
    data object LocalDigitAlreadyAssociatedWithRoman : SetTranslationUseCaseError
}

class SetTranslationUseCase(
    private val translationProvider: TranslationProvider,
    private val translationPersister: TranslationPersister,
) {
    fun execute(localDigit: LocalDigit, romanDigit: RomanDigit) = either<SetTranslationUseCaseError, Unit> {
        translationProvider.getTranslation(localDigit)?.let { persistedDigit ->
            return if (romanDigit != persistedDigit) {
                SetTranslationUseCaseError.LocalDigitAlreadyAssociatedWithRoman.left()
            } else {
                // do not overwrite record with the same values
                Unit.right()
            }
        } ?: translationPersister.associate(localDigit, romanDigit)
    }
}

fun interface TranslationPersister {
    fun associate(localDigit: LocalDigit, romanDigit: RomanDigit)
}
