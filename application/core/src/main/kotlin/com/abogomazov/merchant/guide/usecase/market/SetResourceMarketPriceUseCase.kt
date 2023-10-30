package com.abogomazov.merchant.guide.usecase.market

import arrow.core.Either
import com.abogomazov.merchant.guide.domain.galaxy.GalaxyNumeral
import com.abogomazov.merchant.guide.domain.galaxy.GalaxyNumber
import com.abogomazov.merchant.guide.domain.market.Credits
import com.abogomazov.merchant.guide.domain.market.Resource
import com.abogomazov.merchant.guide.domain.market.UnitPrice
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

    fun execute(
        totalResourceAmount: GalaxyNumber,
        resource: Resource,
        totalPrice: Credits
    ): Either<SetResourceMarketPriceError, Unit> {
        return evaluator.evaluate(totalResourceAmount).map { quantity ->
            marketPriceProvider.getUnitPrice(resource)?.let {
                logger.info("Price already saved for $resource. Deleting.")
                marketPriceRemover.remove(resource)
            }
            val price = UnitPrice.calculate(totalPrice, quantity)
            logger.info("Calculated price is $price for quantity=$quantity and totalPrice=$totalPrice")
            logger.info("Saving price of $resource")
            marketPricePersister.setPrice(
                resource = resource,
                price = price
            )
            logger.info("Set $resource price=$price")
        }.mapLeft { it.toError() }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(SetResourceMarketPriceUseCase::class.java)
    }
}

fun interface MarketPriceRemover {
    fun remove(resource: Resource)
}

fun interface MarketPricePersister {
    fun setPrice(resource: Resource, price: UnitPrice)
}

private fun GalaxyNumberEvaluationError.toError() =
    when (this) {
        is GalaxyNumberEvaluationError.TranslationNotFound ->
            SetResourceMarketPriceError.TranslationNotFound(this.digit)
        GalaxyNumberEvaluationError.RomanNotationRulesViolated -> SetResourceMarketPriceError.RomanNotationRulesViolated
    }
