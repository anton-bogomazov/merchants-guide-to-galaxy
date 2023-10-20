package com.abogomazov.merchant.guide.usecase.market

import arrow.core.raise.either
import com.abogomazov.merchant.guide.domain.local.LocalNumber
import com.abogomazov.merchant.guide.domain.market.Credits
import com.abogomazov.merchant.guide.domain.market.Resource
import com.abogomazov.merchant.guide.domain.market.UnitPrice
import com.abogomazov.merchant.guide.usecase.common.LocalNumberEvaluator
import com.abogomazov.merchant.guide.usecase.common.LocalNumberEvaluatorError
import com.abogomazov.merchant.guide.usecase.market.SetResourceMarketPriceUseCaseError.*

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
    ) = either<SetResourceMarketPriceUseCaseError, Unit> {
        return evaluator.evaluate(totalResourceAmount).map { totalAmount ->
            val unitPrice = UnitPrice.calculate(totalPrice, totalAmount)
            marketPricePersister.setPrice(resource, unitPrice)
        }.mapLeft { it.toError() }
    }

}

fun interface MarketPricePersister {
    fun setPrice(resource: Resource, price: UnitPrice)
}

private fun LocalNumberEvaluatorError.toError() =
    when (this) {
        LocalNumberEvaluatorError.TranslationNotFound -> TranslationNotFound
        LocalNumberEvaluatorError.NumberIsNotFollowingRomanNotationRules -> NumberIsNotFollowingRomanNotationRules
    }
