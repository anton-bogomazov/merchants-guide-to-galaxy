package com.abogomazov.merchant.guide.storage.inmemory

import com.abogomazov.merchant.guide.domain.market.Resource
import com.abogomazov.merchant.guide.domain.market.UnitPrice
import com.abogomazov.merchant.guide.usecase.market.MarketPricePersister
import com.abogomazov.merchant.guide.usecase.market.MarketPriceProvider
import com.abogomazov.merchant.guide.usecase.market.MarketPriceRemover

class MarketInMemoryRepository : MarketPriceProvider, MarketPriceRemover, MarketPricePersister {

    private val prices = KeyValueInMemoryStorage<Resource, UnitPrice>()

    override fun getUnitPrice(resource: Resource) = prices.get(resource)

    override fun remove(resource: Resource) {
        prices.delete(resource)
    }

    override fun setPrice(resource: Resource, price: UnitPrice) {
        prices.set(resource, price)
    }
}
