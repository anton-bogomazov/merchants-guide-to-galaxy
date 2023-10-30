package com.abogomazov.merchant.guide.usecase.market

import arrow.core.left
import arrow.core.raise.either
import arrow.core.right
import com.abogomazov.merchant.guide.domain.galaxy.GalaxyNumeral
import com.abogomazov.merchant.guide.domain.galaxy.GalaxyNumber
import com.abogomazov.merchant.guide.domain.market.Credits
import com.abogomazov.merchant.guide.domain.market.Resource
import com.abogomazov.merchant.guide.usecase.common.GalaxyNumberEvaluationError
import com.abogomazov.merchant.guide.usecase.common.GalaxyNumberEvaluator
import org.slf4j.LoggerFactory

sealed interface GetResourceMarketPriceError {
    data object PriceNotFound : GetResourceMarketPriceError
    data class TranslationNotFound(val digit: GalaxyNumeral) : GetResourceMarketPriceError
    data object RomanNotationRulesViolated : GetResourceMarketPriceError
}

class GetResourceMarketPriceUseCase(
    private val evaluator: GalaxyNumberEvaluator,
    private val marketPriceProvider: MarketPriceProvider,
) {

    fun execute(resource: Resource, resourceQuantity: GalaxyNumber) =
        either {
            evaluator.amountOf(resourceQuantity).mapLeft { it.toError() }.bind() to
            getPriceOrError(resource).bind()
        }.map { (quantity, price) ->
            Credits.total(quantity, price)
                .also { logger.info("Cost of amount=$resourceQuantity of $resource is $it") }
        }

    private fun getPriceOrError(resource: Resource) =
        marketPriceProvider.getUnitPrice(resource)?.right()
            ?: GetResourceMarketPriceError.PriceNotFound.left()
                .also { logger.error("Price not found for resource=$resource") }

    companion object {
        private val logger = LoggerFactory.getLogger(GetResourceMarketPriceUseCase::class.java)
    }
}

private fun GalaxyNumberEvaluationError.toError() =
    when (this) {
        is GalaxyNumberEvaluationError.TranslationNotFound ->
            GetResourceMarketPriceError.TranslationNotFound(this.digit)
        GalaxyNumberEvaluationError.RomanNotationRulesViolated ->
            GetResourceMarketPriceError.RomanNotationRulesViolated
    }
