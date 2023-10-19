package com.abogomazov.merchant.guide.cli.commands

import com.abogomazov.merchant.guide.domain.five
import com.abogomazov.merchant.guide.domain.roman.RomanDigit
import com.abogomazov.merchant.guide.domain.setTranslationError
import com.abogomazov.merchant.guide.domain.setTranslationResult
import com.abogomazov.merchant.guide.usecase.translator.SetTranslationUseCase
import com.abogomazov.merchant.guide.usecase.translator.SetTranslationUseCaseError
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk


class SetTranslationCommandTest : FreeSpec({

    "sucessfully executed usecase leads to result response" {
        val usecase = mockk<SetTranslationUseCase>()
        every { usecase.execute(five(), RomanDigit.V) } returns setTranslationResult()
        val sut = SetTranslationCommand(five(), RomanDigit.V)

        sut.execute(usecase) shouldBe ""
    }

    "executed with error usecase leads to error response" {
        val usecase = mockk<SetTranslationUseCase>()
        every { usecase.execute(five(), RomanDigit.V) } returns
                setTranslationError(SetTranslationUseCaseError.LocalDigitAlreadyAssociatedWithRoman)
        val sut = SetTranslationCommand(five(), RomanDigit.V)

        sut.execute(usecase) shouldBe "[Error] LocalDigitAlreadyAssociatedWithRoman"
    }

})