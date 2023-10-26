package com.abogomazov.merchant.guide.cli.commands

import com.abogomazov.merchant.guide.cli.setTranslationError
import com.abogomazov.merchant.guide.cli.setTranslationResult
import com.abogomazov.merchant.guide.domain.five
import com.abogomazov.merchant.guide.domain.roman.RomanNumeral
import com.abogomazov.merchant.guide.usecase.translator.SetTranslationError
import com.abogomazov.merchant.guide.usecase.translator.SetTranslationUseCase
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class SetTranslationCommandTest : FreeSpec({

    "sucessfully executed usecase leads to result response" {
        val usecase = mockk<SetTranslationUseCase>()
        every { usecase.execute(five(), RomanNumeral.V) } returns setTranslationResult()
        val sut = SetTranslationCommand(five(), RomanNumeral.V)

        sut.execute(usecase) shouldBe "Set"
    }

    "executed with error usecase leads to error response" {
        val usecase = mockk<SetTranslationUseCase>()
        every { usecase.execute(five(), RomanNumeral.V) } returns
                setTranslationError(SetTranslationError.GalaxyNumeralAlreadyAssociated(RomanNumeral.I))
        val sut = SetTranslationCommand(five(), RomanNumeral.V)

        sut.execute(usecase) shouldBe "[Error] Digit \"five\" is already associated with \"I\""
    }
})