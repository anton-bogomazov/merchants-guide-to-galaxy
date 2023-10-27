package com.abogomazov.merchant.guide.cli.commands

import com.abogomazov.merchant.guide.cli.getResourceError
import com.abogomazov.merchant.guide.cli.getResourceResult
import com.abogomazov.merchant.guide.domain.credits
import com.abogomazov.merchant.guide.domain.dirt
import com.abogomazov.merchant.guide.domain.galaxyFour
import com.abogomazov.merchant.guide.usecase.market.GetResourceMarketPriceError
import com.abogomazov.merchant.guide.usecase.market.GetResourceMarketPriceUseCase
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class GetResourceMarketPriceCommandTest : FreeSpec({

    "sucessfully executed usecase leads to result response" {
        val usecase = mockk<GetResourceMarketPriceUseCase>()
        every { usecase.execute(galaxyFour(), dirt()) } returns getResourceResult(credits(43))
        val sut = GetResourceMarketPriceCommand(galaxyFour(), dirt())

        sut.execute(usecase) shouldBe "one five Dirt is 43 Credits"
    }

    "executed with error usecase leads to error response" {
        val usecase = mockk<GetResourceMarketPriceUseCase>()
        every { usecase.execute(galaxyFour(), dirt()) } returns
                getResourceError(GetResourceMarketPriceError.PriceNotFound)
        val sut = GetResourceMarketPriceCommand(galaxyFour(), dirt())

        sut.execute(usecase) shouldBe "[Error] Price not found for \"Dirt\" resource"
    }
})
