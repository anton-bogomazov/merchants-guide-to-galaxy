package com.abogomazov.merchant.guide.cli.commands

import com.abogomazov.merchant.guide.domain.amount
import com.abogomazov.merchant.guide.domain.galaxyFour
import com.abogomazov.merchant.guide.domain.getTranslationError
import com.abogomazov.merchant.guide.domain.getTranslationResult
import com.abogomazov.merchant.guide.domain.one
import com.abogomazov.merchant.guide.usecase.translator.GetTranslationError
import com.abogomazov.merchant.guide.usecase.translator.GetTranslationUseCase
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class GetTranslationCommandTest : FreeSpec({

    "sucessfully executed usecase leads to result response" {
        val usecase = mockk<GetTranslationUseCase>()
        every { usecase.execute(galaxyFour()) } returns getTranslationResult(amount(4))
        val sut = GetTranslationCommand(galaxyFour())

        sut.execute(usecase) shouldBe "one five is 4"
    }

    "executed with error usecase leads to error response" {
        val usecase = mockk<GetTranslationUseCase>()
        every { usecase.execute(galaxyFour()) } returns
                getTranslationError(GetTranslationError.TranslationNotFound(one()))
        val sut = GetTranslationCommand(galaxyFour())

        sut.execute(usecase) shouldBe "[Error] Translation not found for \"one\""
    }
})
