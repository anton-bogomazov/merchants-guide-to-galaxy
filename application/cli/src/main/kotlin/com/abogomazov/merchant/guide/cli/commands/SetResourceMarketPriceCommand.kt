package com.abogomazov.merchant.guide.cli.commands

import com.abogomazov.merchant.guide.domain.galaxy.GalaxyNumber
import com.abogomazov.merchant.guide.domain.market.Credits
import com.abogomazov.merchant.guide.domain.market.Resource
import com.abogomazov.merchant.guide.usecase.market.SetResourceMarketPriceError
import com.abogomazov.merchant.guide.usecase.market.SetResourceMarketPriceUseCase

data class SetResourceMarketPriceCommand(
    private val resourceAmount: GalaxyNumber,
    private val resource: Resource,
    private val total: Credits,
) : BusinessCommand {

    fun execute(usecase: SetResourceMarketPriceUseCase) =
        usecase.execute(resource, resourceAmount, total)
            .fold({ it.toError() }, { "Set" })

    private fun SetResourceMarketPriceError.toError() =
        when (this) {
            is SetResourceMarketPriceError.TranslationNotFound -> translationNotFound(this.digit)
            SetResourceMarketPriceError.RomanNotationRulesViolated -> romanNotationRulesViolated(resourceAmount)
        }
}
