package com.abogomazov.merchant.guide.usecase.translator

import com.abogomazov.merchant.guide.domain.emptyDictionary
import com.abogomazov.merchant.guide.domain.englishDictionary
import com.abogomazov.merchant.guide.domain.fifty
import com.abogomazov.merchant.guide.domain.five
import com.abogomazov.merchant.guide.domain.local.LocalNumber
import com.abogomazov.merchant.guide.domain.one
import com.abogomazov.merchant.guide.domain.ten
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class GetTranslationUseCaseTest : FreeSpec({

    "uses provided dictionary to translate to roman number" {
        val localNumber = LocalNumber(
            listOf(fifty(), ten(), ten(), ten(), five(), one(), one(), one())
        )

        GetTranslationUseCase(englishDictionary())
            .execute(localNumber)
            .shouldBeRight()
            .toInt() shouldBe 88
    }

    "local number representing invalid roman number cannot be evaluated" {
        val localNumber = LocalNumber(
            listOf(one(), one(), five())
        )

        GetTranslationUseCase(englishDictionary())
            .execute(localNumber)
            .shouldBeLeft(GetTranslationUseCaseError.NumberIsNotFollowingRomanNotationRules)
    }

    "impossible to translate number with insufficient dictionary" {
        val localNumber = LocalNumber(
            listOf(one(), one(), five())
        )

        GetTranslationUseCase(emptyDictionary())
            .execute(localNumber)
            .shouldBeLeft(GetTranslationUseCaseError.TranslationNotFound)
    }
})
