package com.abogomazov.merchant.guide.application.inmemory

import com.abogomazov.merchant.guide.domain.market.Resource
import com.abogomazov.merchant.guide.domain.market.UnitPrice
import com.abogomazov.merchant.guide.usecase.market.MarketPricePersister
import com.abogomazov.merchant.guide.usecase.market.MarketPriceProvider

class InMemoryMarket : MarketPriceProvider, MarketPricePersister {
    override fun getUnitPrice(resource: Resource): UnitPrice? {
        TODO("Not yet implemented")
    }

    override fun setPrice(resource: Resource, price: UnitPrice) {
        TODO("Not yet implemented")
    }
}
