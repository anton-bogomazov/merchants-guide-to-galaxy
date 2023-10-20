package com.abogomazov.merchant.guide.usecase.market

import arrow.core.left
import com.abogomazov.merchant.guide.domain.local.LocalDigit
import com.abogomazov.merchant.guide.domain.local.LocalNumber
import com.abogomazov.merchant.guide.domain.market.Credits
import com.abogomazov.merchant.guide.domain.market.Resource
import com.abogomazov.merchant.guide.domain.market.UnitPrice
import com.abogomazov.merchant.guide.usecase.common.LocalNumberEvaluationError
import com.abogomazov.merchant.guide.usecase.common.LocalNumberEvaluator

sealed interface GetResourceMarketPriceError {
    data object PriceNotFound : GetResourceMarketPriceError
    data class TranslationNotFound(val digit: LocalDigit) : GetResourceMarketPriceError
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
        is LocalNumberEvaluationError.TranslationNotFound ->
            GetResourceMarketPriceError.TranslationNotFound(this.digit)
        LocalNumberEvaluationError.RomanNotationRulesViolated ->
            GetResourceMarketPriceError.RomanNotationRulesViolated
    }
