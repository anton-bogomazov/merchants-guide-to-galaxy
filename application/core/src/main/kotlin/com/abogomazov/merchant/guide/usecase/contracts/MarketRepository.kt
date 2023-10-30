package com.abogomazov.merchant.guide.usecase.contracts

import com.abogomazov.merchant.guide.usecase.market.MarketPricePersister
import com.abogomazov.merchant.guide.usecase.market.MarketPriceProvider
import com.abogomazov.merchant.guide.usecase.market.MarketPriceRemover

interface MarketRepository : MarketPricePersister, MarketPriceProvider, MarketPriceRemover
