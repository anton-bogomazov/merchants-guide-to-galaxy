package com.abogomazov.merchant.guide.cli.commands

import com.abogomazov.merchant.guide.domain.local.LocalNumber
import com.abogomazov.merchant.guide.domain.market.Credits
import com.abogomazov.merchant.guide.domain.market.Resource
import com.abogomazov.merchant.guide.usecase.market.SetResourceMarketPriceUseCase
import com.abogomazov.merchant.guide.usecase.market.SetResourceMarketPriceUseCaseError

data class SetResourceMarketPriceCommand(
    private val resourceAmount: LocalNumber,
    private val resource: Resource,
    private val total: Credits,
) : BusinessCommand {

    fun execute(usecase: SetResourceMarketPriceUseCase) =
        usecase.execute(resourceAmount, resource, total)
            .fold({ it.toError() }, { response() })

    private fun response() = "Set"

}

private fun SetResourceMarketPriceUseCaseError.toError() =
    when (this) {
        SetResourceMarketPriceUseCaseError.TranslationNotFound -> errorMessage(this)
        SetResourceMarketPriceUseCaseError.NumberIsNotFollowingRomanNotationRules -> errorMessage(this)
    }
