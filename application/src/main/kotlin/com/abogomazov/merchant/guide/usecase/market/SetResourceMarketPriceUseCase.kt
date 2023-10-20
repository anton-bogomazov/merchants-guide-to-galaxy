package com.abogomazov.merchant.guide.usecase.market

import arrow.core.Either
import com.abogomazov.merchant.guide.domain.local.LocalDigit
import com.abogomazov.merchant.guide.domain.local.LocalNumber
import com.abogomazov.merchant.guide.domain.market.Credits
import com.abogomazov.merchant.guide.domain.market.Resource
import com.abogomazov.merchant.guide.domain.market.UnitPrice
import com.abogomazov.merchant.guide.usecase.common.LocalNumberEvaluationError
import com.abogomazov.merchant.guide.usecase.common.LocalNumberEvaluator
import org.slf4j.LoggerFactory

sealed interface SetResourceMarketPriceError {
    data class TranslationNotFound(val digit: LocalDigit) : SetResourceMarketPriceError
    data object RomanNotationRulesViolated : SetResourceMarketPriceError
}

class SetResourceMarketPriceUseCase(
    private val evaluator: LocalNumberEvaluator,
    private val marketPricePersister: MarketPricePersister,
) {

    fun execute(
        totalResourceAmount: LocalNumber,
        resource: Resource,
        totalPrice: Credits
    ): Either<SetResourceMarketPriceError, Unit> {
        logger.info("Saving price of $resource")
        return evaluator.evaluate(totalResourceAmount).map { quantity ->
            val price = UnitPrice.calculate(totalPrice, quantity)
            logger.info("Calculated price is $price for quantity=$quantity and totalPrice=$totalPrice")
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

fun interface MarketPricePersister {
    fun setPrice(resource: Resource, price: UnitPrice)
}

private fun LocalNumberEvaluationError.toError() =
    when (this) {
        is LocalNumberEvaluationError.TranslationNotFound ->
            SetResourceMarketPriceError.TranslationNotFound(this.digit)
        LocalNumberEvaluationError.RomanNotationRulesViolated -> SetResourceMarketPriceError.RomanNotationRulesViolated
    }
