package com.abogomazov.merchant.guide.usecase.market

import arrow.core.left
import arrow.core.raise.either
import arrow.core.right
import com.abogomazov.merchant.guide.domain.local.LocalNumber
import com.abogomazov.merchant.guide.domain.market.Credits
import com.abogomazov.merchant.guide.domain.market.Resource
import com.abogomazov.merchant.guide.domain.market.UnitPrice
import com.abogomazov.merchant.guide.usecase.common.LocalNumberEvaluator
import com.abogomazov.merchant.guide.usecase.common.LocalNumberEvaluatorError
import com.abogomazov.merchant.guide.usecase.market.GetResourceMarketPriceUseCaseError.*

sealed interface GetResourceMarketPriceUseCaseError {
    data object PriceNotFound : GetResourceMarketPriceUseCaseError
    data object TranslationNotFound : GetResourceMarketPriceUseCaseError
    data object NumberIsNotFollowingRomanNotationRules : GetResourceMarketPriceUseCaseError
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
                    ?: return PriceNotFound.left()
            )
        }.mapLeft { it.toError() }

}

fun interface MarketPriceProvider {
    fun getUnitPrice(resource: Resource): UnitPrice?
}

private fun LocalNumberEvaluatorError.toError() =
    when (this) {
        LocalNumberEvaluatorError.TranslationNotFound -> TranslationNotFound
        LocalNumberEvaluatorError.NumberIsNotFollowingRomanNotationRules -> NumberIsNotFollowingRomanNotationRules
    }

