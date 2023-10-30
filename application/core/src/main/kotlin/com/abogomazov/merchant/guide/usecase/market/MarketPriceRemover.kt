package com.abogomazov.merchant.guide.usecase.market

import com.abogomazov.merchant.guide.domain.market.Resource

fun interface MarketPriceRemover {
    fun remove(resource: Resource)
}
