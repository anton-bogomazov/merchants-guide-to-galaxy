package com.abogomazov.merchant.guide.cli.commands

import com.abogomazov.merchant.guide.cli.setResourceError
import com.abogomazov.merchant.guide.cli.setResourceResult
import com.abogomazov.merchant.guide.domain.credits
import com.abogomazov.merchant.guide.domain.dirt
import com.abogomazov.merchant.guide.domain.galaxyFour
import com.abogomazov.merchant.guide.domain.one
import com.abogomazov.merchant.guide.usecase.market.SetResourceMarketPriceError
import com.abogomazov.merchant.guide.usecase.market.SetResourceMarketPriceUseCase
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class SetResourceMarketPriceCommandTest : FreeSpec({

    "sucessfully executed usecase leads to result response" {
        val usecase = mockk<SetResourceMarketPriceUseCase>()
        every { usecase.execute(galaxyFour(), dirt(), credits(43)) } returns setResourceResult()
        val sut = SetResourceMarketPriceCommand(galaxyFour(), dirt(), credits(43))

        sut.execute(usecase) shouldBe "Set"
    }

    "executed with error usecase leads to error response" {
        val usecase = mockk<SetResourceMarketPriceUseCase>()
        every { usecase.execute(galaxyFour(), dirt(), credits(43)) } returns
                setResourceError(SetResourceMarketPriceError.TranslationNotFound(one()))
        val sut = SetResourceMarketPriceCommand(galaxyFour(), dirt(), credits(43))

        sut.execute(usecase) shouldBe "[Error] Translation not found for \"one\""
    }
})
