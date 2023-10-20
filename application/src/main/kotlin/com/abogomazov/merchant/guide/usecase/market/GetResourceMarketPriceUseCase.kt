package com.abogomazov.merchant.guide.usecase.market

import arrow.core.Either
import arrow.core.left
import com.abogomazov.merchant.guide.domain.local.LocalDigit
import com.abogomazov.merchant.guide.domain.local.LocalNumber
import com.abogomazov.merchant.guide.domain.market.Credits
import com.abogomazov.merchant.guide.domain.market.Resource
import com.abogomazov.merchant.guide.domain.market.UnitPrice
import com.abogomazov.merchant.guide.usecase.common.LocalNumberEvaluationError
import com.abogomazov.merchant.guide.usecase.common.LocalNumberEvaluator
import org.slf4j.LoggerFactory

sealed interface GetResourceMarketPriceError {
    data object PriceNotFound : GetResourceMarketPriceError
    data class TranslationNotFound(val digit: LocalDigit) : GetResourceMarketPriceError
    data object RomanNotationRulesViolated : GetResourceMarketPriceError
}

class GetResourceMarketPriceUseCase(
    private val evaluator: LocalNumberEvaluator,
    private val marketPriceProvider: MarketPriceProvider,
) {

    fun execute(
        amountOfResource: LocalNumber,
        resource: Resource
    ): Either<GetResourceMarketPriceError, Credits> {
        logger.info("Getting cost of amount=$amountOfResource of $resource")
        return evaluator.evaluate(amountOfResource).map { quantity ->
            val price = marketPriceProvider.getUnitPrice(resource)
            if (price == null) {
                logger.error("Price not found for resource=$resource")
                return GetResourceMarketPriceError.PriceNotFound.left()
            }
            Credits.total(
                quantity = quantity,
                price = price
            ).also {
                logger.info("Cost of amount=$amountOfResource of $resource is $it")
            }
        }.mapLeft { it.toError() }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(GetResourceMarketPriceUseCase::class.java)
    }
}

fun interface MarketPriceProvider {
    fun getUnitPrice(resource: Resource): UnitPrice?
}

private fun LocalNumberEvaluationError.toError() =
    when (this) {
        is LocalNumberEvaluationError.TranslationNotFound ->
            GetResourceMarketPriceError.TranslationNotFound(this.digit)
        LocalNumberEvaluationError.RomanNotationRulesViolated ->
            GetResourceMarketPriceError.RomanNotationRulesViolated
    }
