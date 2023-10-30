package com.abogomazov.merchant.guide.usecase.market

import arrow.core.raise.either
import com.abogomazov.merchant.guide.domain.galaxy.GalaxyNumeral
import com.abogomazov.merchant.guide.domain.galaxy.GalaxyNumber
import com.abogomazov.merchant.guide.domain.market.Credits
import com.abogomazov.merchant.guide.domain.market.Resource
import com.abogomazov.merchant.guide.domain.market.UnitPrice
import com.abogomazov.merchant.guide.domain.roman.Amount
import com.abogomazov.merchant.guide.usecase.common.GalaxyNumberEvaluationError
import com.abogomazov.merchant.guide.usecase.common.GalaxyNumberEvaluator
import org.slf4j.LoggerFactory

sealed interface SetResourceMarketPriceError {
    data class TranslationNotFound(val digit: GalaxyNumeral) : SetResourceMarketPriceError
    data object RomanNotationRulesViolated : SetResourceMarketPriceError
}

class SetResourceMarketPriceUseCase(
    private val evaluator: GalaxyNumberEvaluator,
    private val marketPricePersister: MarketPricePersister,
    private val marketPriceProvider: MarketPriceProvider,
    private val marketPriceRemover: MarketPriceRemover,
) {

    fun execute(resource: Resource, resourceQuantity: GalaxyNumber, resourceCost: Credits) =
        either { evaluator.evaluate(resourceQuantity).bind() }.mapLeft { it.toError() }
            .map { quantity ->
                removePriceIfExists(resource)
                val price = calculateResourcePrice(quantity, resourceCost)
                saveResourcePrice(resource, price)
            }

    private fun removePriceIfExists(resource: Resource): Unit? =
        marketPriceProvider.getUnitPrice(resource)?.let {
            marketPriceRemover.remove(resource)
        }?.also { logger.info("Price already saved for $resource. Deleting.") }

    private fun calculateResourcePrice(quantity: Amount, cost: Credits) =
        UnitPrice.calculate(cost, quantity)
            .also { logger.info("Calculated price is $it for quantity=$quantity and totalPrice=$cost") }

    private fun saveResourcePrice(resource: Resource, price: UnitPrice) =
        marketPricePersister.setPrice(
            resource = resource,
            price = price
        ).also { logger.info("Set $resource price=$price") }

    companion object {
        private val logger = LoggerFactory.getLogger(SetResourceMarketPriceUseCase::class.java)
    }
}

private fun GalaxyNumberEvaluationError.toError() =
    when (this) {
        is GalaxyNumberEvaluationError.TranslationNotFound ->
            SetResourceMarketPriceError.TranslationNotFound(this.digit)
        GalaxyNumberEvaluationError.RomanNotationRulesViolated -> SetResourceMarketPriceError.RomanNotationRulesViolated
    }
