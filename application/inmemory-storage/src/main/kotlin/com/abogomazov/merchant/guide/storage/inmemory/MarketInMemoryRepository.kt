package com.abogomazov.merchant.guide.storage.inmemory

import com.abogomazov.merchant.guide.domain.market.Resource
import com.abogomazov.merchant.guide.domain.market.UnitPrice
import com.abogomazov.merchant.guide.usecase.contracts.MarketRepository

class MarketInMemoryRepository : MarketRepository {

    private val prices = KeyValueInMemoryStorage<Resource, UnitPrice>()

    override fun getUnitPrice(resource: Resource) = prices.get(resource)

    override fun setPrice(resource: Resource, price: UnitPrice) {
        prices.set(resource, price)
    }
}
