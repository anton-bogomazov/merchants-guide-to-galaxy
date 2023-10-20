package com.abogomazov.merchant.guide.usecase.market

import com.abogomazov.merchant.guide.domain.credits
import com.abogomazov.merchant.guide.domain.dirt
import com.abogomazov.merchant.guide.domain.englishNumberEvaluator
import com.abogomazov.merchant.guide.domain.localFour
import com.abogomazov.merchant.guide.domain.price
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FreeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class SetResourceMarketPriceTest : FreeSpec({

    "uses provided dictionary to translate to amount" {
        val persister = mockk<MarketPricePersister>()
        val sut = SetResourceMarketPriceUseCase(englishNumberEvaluator(), persister)
        every { persister.setPrice(any(), any()) } answers { }

        sut.execute(localFour(), dirt(), credits(50)).shouldBeRight()

        verify { persister.setPrice(dirt(), price(12.5f)) }
    }
})
