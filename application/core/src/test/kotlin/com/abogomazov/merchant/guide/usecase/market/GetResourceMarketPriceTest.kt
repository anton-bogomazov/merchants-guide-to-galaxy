package com.abogomazov.merchant.guide.usecase.market

import com.abogomazov.merchant.guide.domain.credits
import com.abogomazov.merchant.guide.domain.dirt
import com.abogomazov.merchant.guide.domain.englishNumberEvaluator
import com.abogomazov.merchant.guide.domain.galaxyThree
import com.abogomazov.merchant.guide.domain.steamDeck
import com.abogomazov.merchant.guide.domain.waterDirtMarket
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FreeSpec

class GetResourceMarketPriceTest : FreeSpec({

    "uses provided dictionary to translate to amount" {
        val sut = GetResourceMarketPriceUseCase(englishNumberEvaluator(), waterDirtMarket())

        sut.execute(dirt(), galaxyThree())
            .shouldBeRight(credits(4))
    }

    "impossible to get price if no price for the unit on market" {
        val sut = GetResourceMarketPriceUseCase(englishNumberEvaluator(), waterDirtMarket())

        sut.execute(steamDeck(), galaxyThree())
            .shouldBeLeft(GetResourceMarketPriceError.PriceNotFound)
    }
})
