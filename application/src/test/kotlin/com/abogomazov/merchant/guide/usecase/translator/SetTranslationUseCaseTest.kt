package com.abogomazov.merchant.guide.usecase.translator

import com.abogomazov.merchant.guide.domain.englishDictionary
import com.abogomazov.merchant.guide.domain.inMemoryTranslationStorage
import com.abogomazov.merchant.guide.domain.one
import com.abogomazov.merchant.guide.domain.roman.RomanNumeral
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FreeSpec

class SetTranslationUseCaseTest : FreeSpec({

    "uses provided dictionary to translate to amount" {
        val dictionary = inMemoryTranslationStorage()
        val sut = SetTranslationUseCase(englishDictionary(), dictionary, dictionary)

        sut.execute(one(), RomanNumeral.I)
            .shouldBeRight()
    }

    "galaxy duplicated numerals are not allowed" {
        val dictionary = inMemoryTranslationStorage(one() to RomanNumeral.V)
        val sut = SetTranslationUseCase(dictionary, dictionary, dictionary)

        sut.execute(one(), RomanNumeral.I)
            .shouldBeLeft(SetTranslationError.GalaxyNumeralAlreadyAssociated(RomanNumeral.V))
    }

    "it safe to try to overwrite association with the same value" {
        val dictionary = inMemoryTranslationStorage(one() to RomanNumeral.I)
        val sut = SetTranslationUseCase(dictionary, dictionary, dictionary)

        sut.execute(one(), RomanNumeral.I)
            .shouldBeRight()
    }
})
