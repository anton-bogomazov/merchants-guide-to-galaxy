package com.abogomazov.merchant.guide.usecase.market

import com.abogomazov.merchant.guide.domain.market.Resource
import com.abogomazov.merchant.guide.domain.market.UnitPrice

fun interface MarketPricePersister {
    fun setPrice(resource: Resource, price: UnitPrice)
}
