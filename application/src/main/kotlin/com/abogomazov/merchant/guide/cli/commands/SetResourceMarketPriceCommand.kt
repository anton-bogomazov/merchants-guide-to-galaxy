package com.abogomazov.merchant.guide.cli.commands

import com.abogomazov.merchant.guide.domain.local.LocalNumber
import com.abogomazov.merchant.guide.domain.market.Credits
import com.abogomazov.merchant.guide.domain.market.Resource
import com.abogomazov.merchant.guide.usecase.market.SetResourceMarketPriceError
import com.abogomazov.merchant.guide.usecase.market.SetResourceMarketPriceUseCase

data class SetResourceMarketPriceCommand(
    private val resourceAmount: LocalNumber,
    private val resource: Resource,
    private val total: Credits,
) : BusinessCommand {

    fun execute(usecase: SetResourceMarketPriceUseCase) =
        usecase.execute(resourceAmount, resource, total)
            .fold({ it.toError() }, { "Set" })

    private fun SetResourceMarketPriceError.toError() =
        when (this) {
            is SetResourceMarketPriceError.TranslationNotFound -> translationNotFound(this.digit)
            SetResourceMarketPriceError.RomanNotationRulesViolated -> romanNotationRulesViolated(resourceAmount)
        }
}
