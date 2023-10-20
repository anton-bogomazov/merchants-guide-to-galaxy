package com.abogomazov.merchant.guide.usecase.market

import arrow.core.left
import com.abogomazov.merchant.guide.domain.local.LocalNumber
import com.abogomazov.merchant.guide.domain.market.Credits
import com.abogomazov.merchant.guide.domain.market.Resource
import com.abogomazov.merchant.guide.domain.market.UnitPrice
import com.abogomazov.merchant.guide.usecase.common.LocalNumberEvaluator
import com.abogomazov.merchant.guide.usecase.common.LocalNumberEvaluationError


sealed interface GetResourceMarketPriceError {
    data object PriceNotFound : GetResourceMarketPriceError
    data object TranslationNotFound : GetResourceMarketPriceError
    data object RomanNotationRulesViolated : GetResourceMarketPriceError
}

class GetResourceMarketPriceUseCase(
    private val evaluator: LocalNumberEvaluator,
    private val marketPriceProvider: MarketPriceProvider,
) {

    fun execute(amountOfResource: LocalNumber, resource: Resource) =
        evaluator.evaluate(amountOfResource).map { quantity ->
            Credits.total(
                quantity = quantity,
                price = marketPriceProvider.getUnitPrice(resource)
                    ?: return GetResourceMarketPriceError.PriceNotFound.left()
            )
        }.mapLeft { it.toError() }

}

fun interface MarketPriceProvider {
    fun getUnitPrice(resource: Resource): UnitPrice?
}

private fun LocalNumberEvaluationError.toError() =
    when (this) {
        LocalNumberEvaluationError.TranslationNotFound ->
            GetResourceMarketPriceError.TranslationNotFound
        LocalNumberEvaluationError.RomanNotationRulesViolated ->
            GetResourceMarketPriceError.RomanNotationRulesViolated
    }

