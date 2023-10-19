package com.abogomazov.merchant.guide.usecase.translator

import com.abogomazov.merchant.guide.domain.englishDictionary
import com.abogomazov.merchant.guide.domain.inMemoryTranslationStorage
import com.abogomazov.merchant.guide.domain.one
import com.abogomazov.merchant.guide.domain.roman.RomanDigit
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FreeSpec

class SetTranslationUseCaseTest : FreeSpec({

    "uses provided dictionary to translate to amount" {
        val sut = SetTranslationUseCase(englishDictionary(), inMemoryTranslationStorage())

        sut.execute(one(), RomanDigit.I)
            .shouldBeRight()
    }

    "local duplicated numerals are not allowed" {
        val dictionary = inMemoryTranslationStorage(one() to RomanDigit.V)
        val sut = SetTranslationUseCase(dictionary, dictionary)

        sut.execute(one(), RomanDigit.I)
            .shouldBeLeft(SetTranslationUseCaseError.LocalDigitAlreadyAssociatedWithRoman)
    }

    "it safe to try to overwrite association with the same value" {
        val dictionary = inMemoryTranslationStorage(one() to RomanDigit.I)
        val sut = SetTranslationUseCase(dictionary, dictionary)

        sut.execute(one(), RomanDigit.I)
            .shouldBeRight()
    }

})