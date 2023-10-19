package com.abogomazov.merchant.guide.application.inmemory

import com.abogomazov.merchant.guide.domain.market.Resource
import com.abogomazov.merchant.guide.domain.market.UnitPrice
import com.abogomazov.merchant.guide.usecase.market.MarketPricePersister
import com.abogomazov.merchant.guide.usecase.market.MarketPriceProvider

class InMemoryMarket : MarketPriceProvider, MarketPricePersister {

    private val prices = mutableMapOf<Resource, UnitPrice>()

    override fun getUnitPrice(resource: Resource): UnitPrice? {
        return prices[resource]
    }

    override fun setPrice(resource: Resource, price: UnitPrice) {
        prices[resource] = price
    }
}
