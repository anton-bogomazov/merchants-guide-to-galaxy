package com.abogomazov.merchant.guide.usecase.market

import com.abogomazov.merchant.guide.domain.credits
import com.abogomazov.merchant.guide.domain.dirt
import com.abogomazov.merchant.guide.domain.englishNumberEvaluator
import com.abogomazov.merchant.guide.domain.galaxyFour
import com.abogomazov.merchant.guide.domain.price
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FreeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class SetResourceMarketPriceTest : FreeSpec({

    "saves unit price calculated based on cost and quantity" {
        val persister = mockk<MarketPricePersister>()
        val provider = mockk<MarketPriceProvider>()
        val remover = mockk<MarketPriceRemover>()
        val sut = SetResourceMarketPriceUseCase(englishNumberEvaluator(), persister, provider, remover)
        every { persister.setPrice(any(), any()) } answers { }
        every { provider.getUnitPrice(any()) } answers { null }

        sut.execute(galaxyFour(), dirt(), credits(50)).shouldBeRight()

        verify { persister.setPrice(dirt(), price(12.5)) }
    }

    "overwrites unit price if already persisted for resource" {
        val persister = mockk<MarketPricePersister>()
        val provider = mockk<MarketPriceProvider>()
        val remover = mockk<MarketPriceRemover>()
        val sut = SetResourceMarketPriceUseCase(englishNumberEvaluator(), persister, provider, remover)
        every { persister.setPrice(any(), any()) } answers { }
        every { remover.remove(any()) } answers { }
        every { provider.getUnitPrice(any()) } answers { price(34.9) }

        sut.execute(galaxyFour(), dirt(), credits(50)).shouldBeRight()

        verify { persister.setPrice(dirt(), price(12.5)) }
        verify { remover.remove(dirt()) }
    }
})
