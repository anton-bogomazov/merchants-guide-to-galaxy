package com.abogomazov.merchant.guide.usecase.market

import com.abogomazov.merchant.guide.domain.market.Resource
import com.abogomazov.merchant.guide.domain.market.UnitPrice

fun interface MarketPriceProvider {
    fun getUnitPrice(resource: Resource): UnitPrice?
}
