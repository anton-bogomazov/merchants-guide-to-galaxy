package com.abogomazov.merchant.guide.usecase.contracts

import com.abogomazov.merchant.guide.usecase.market.MarketPricePersister
import com.abogomazov.merchant.guide.usecase.market.MarketPriceProvider

interface MarketRepository : MarketPricePersister, MarketPriceProvider
