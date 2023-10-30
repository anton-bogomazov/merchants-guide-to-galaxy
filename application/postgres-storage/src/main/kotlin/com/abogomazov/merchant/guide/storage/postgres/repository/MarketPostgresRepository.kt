package com.abogomazov.merchant.guide.storage.postgres.repository

import com.abogomazov.merchant.guide.domain.market.Resource
import com.abogomazov.merchant.guide.domain.market.UnitPrice
import com.abogomazov.merchant.guide.domain.market.toUnitPrice
import com.abogomazov.merchant.guide.storage.postgres.PostgresDatasource
import com.abogomazov.merchant.guide.usecase.contracts.MarketRepository

class MarketPostgresRepository(
    private val dataSource: PostgresDatasource
) : MarketRepository {

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

    override fun remove(resource: Resource) {
        val query = "DELETE FROM $TABLE WHERE $RESOURCE = '$resource';"
        dataSource.update(query)
    }

    override fun setPrice(resource: Resource, price: UnitPrice) {
        val query = "INSERT INTO $TABLE ($RESOURCE, $PRICE)\n" +
                "VALUES ('$resource', '${price.toBigDecimal()}');"
        dataSource.update(query)
    }
}
