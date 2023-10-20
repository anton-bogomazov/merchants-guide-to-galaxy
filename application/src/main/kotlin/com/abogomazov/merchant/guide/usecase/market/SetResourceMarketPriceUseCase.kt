package com.abogomazov.merchant.guide.usecase.market

import com.abogomazov.merchant.guide.domain.local.LocalDigit
import com.abogomazov.merchant.guide.domain.local.LocalNumber
import com.abogomazov.merchant.guide.domain.market.Credits
import com.abogomazov.merchant.guide.domain.market.Resource
import com.abogomazov.merchant.guide.domain.market.UnitPrice
import com.abogomazov.merchant.guide.usecase.common.LocalNumberEvaluator
import com.abogomazov.merchant.guide.usecase.common.LocalNumberEvaluationError


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
    ) = evaluator.evaluate(totalResourceAmount).map { quantity ->
        marketPricePersister.setPrice(
            resource = resource,
            price = UnitPrice.calculate(totalPrice, quantity)
        )
    }.mapLeft { it.toError() }
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
