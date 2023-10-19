package com.abogomazov.merchant.guide.cli.commands

import com.abogomazov.merchant.guide.domain.credits
import com.abogomazov.merchant.guide.domain.dirt
import com.abogomazov.merchant.guide.domain.localFour
import com.abogomazov.merchant.guide.domain.setResourceError
import com.abogomazov.merchant.guide.domain.setResourceResult
import com.abogomazov.merchant.guide.usecase.market.SetResourceMarketPriceUseCase
import com.abogomazov.merchant.guide.usecase.market.SetResourceMarketPriceUseCaseError
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk


class SetResourceMarketPriceCommandTest : FreeSpec({

    "sucessfully executed usecase leads to result response" {
        val usecase = mockk<SetResourceMarketPriceUseCase>()
        every { usecase.execute(localFour(), dirt(), credits(43)) } returns setResourceResult()
        val sut = SetResourceMarketPriceCommand(localFour(), dirt(), credits(43))

        sut.execute(usecase) shouldBe ""
    }

    "executed with error usecase leads to error response" {
        val usecase = mockk<SetResourceMarketPriceUseCase>()
        every { usecase.execute(localFour(), dirt(), credits(43)) } returns
                setResourceError(SetResourceMarketPriceUseCaseError.TranslationNotFound)
        val sut = SetResourceMarketPriceCommand(localFour(), dirt(), credits(43))

        sut.execute(usecase) shouldBe "[Error] TranslationNotFound"
    }

})
