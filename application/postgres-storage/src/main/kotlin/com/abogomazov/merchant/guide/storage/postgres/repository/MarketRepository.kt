package com.abogomazov.merchant.guide.storage.postgres.repository

import com.abogomazov.merchant.guide.domain.market.Resource
import com.abogomazov.merchant.guide.domain.market.UnitPrice
import com.abogomazov.merchant.guide.domain.market.toUnitPrice
import com.abogomazov.merchant.guide.storage.postgres.PostgresDatasource
import com.abogomazov.merchant.guide.usecase.market.MarketPricePersister
import com.abogomazov.merchant.guide.usecase.market.MarketPriceProvider

class MarketRepository(
    private val dataSource: PostgresDatasource
) : MarketPriceProvider, MarketPricePersister {

    companion object {
        private const val TABLE = "market"

        private const val RESOURCE = "resource_name"
        private const val PRICE = "price"
    }

    override fun getUnitPrice(resource: Resource): UnitPrice? {
        val query = "SELECT * FROM $TABLE WHERE $RESOURCE = '$resource'"
        val result = dataSource.single(query) { it.getString(PRICE) }

        return result?.toUnitPrice()?.fold(
            { error("Database in inconsistent state! Conversion error from $result") },
            { it }
        )
    }

    override fun setPrice(resource: Resource, price: UnitPrice) {
        val query = "INSERT INTO $TABLE ($RESOURCE, $PRICE)\n" +
                "VALUES ('$resource', '${price.toBigDecimal()}');"
        dataSource.update(query)
    }
}
