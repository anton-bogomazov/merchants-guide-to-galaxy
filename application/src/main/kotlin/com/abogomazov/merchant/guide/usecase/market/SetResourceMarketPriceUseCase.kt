package com.abogomazov.merchant.guide.usecase.market

import com.abogomazov.merchant.guide.domain.local.LocalNumber
import com.abogomazov.merchant.guide.domain.market.Credits
import com.abogomazov.merchant.guide.domain.market.Resource
import com.abogomazov.merchant.guide.domain.market.UnitPrice
import com.abogomazov.merchant.guide.usecase.common.LocalNumberEvaluator
import com.abogomazov.merchant.guide.usecase.common.LocalNumberEvaluatorError
import com.abogomazov.merchant.guide.usecase.market.SetResourceMarketPriceUseCaseError.NumberIsNotFollowingRomanNotationRules
import com.abogomazov.merchant.guide.usecase.market.SetResourceMarketPriceUseCaseError.TranslationNotFound

sealed interface SetResourceMarketPriceUseCaseError {
    data object TranslationNotFound : SetResourceMarketPriceUseCaseError
    data object NumberIsNotFollowingRomanNotationRules : SetResourceMarketPriceUseCaseError
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

private fun LocalNumberEvaluatorError.toError() =
    when (this) {
        LocalNumberEvaluatorError.TranslationNotFound -> TranslationNotFound
        LocalNumberEvaluatorError.NumberIsNotFollowingRomanNotationRules -> NumberIsNotFollowingRomanNotationRules
    }
