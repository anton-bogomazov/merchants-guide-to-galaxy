package com.abogomazov.merchant.guide.cli.commands

import com.abogomazov.merchant.guide.domain.galaxy.GalaxyNumber
import com.abogomazov.merchant.guide.domain.market.Credits
import com.abogomazov.merchant.guide.domain.market.Resource
import com.abogomazov.merchant.guide.usecase.market.GetResourceMarketPriceError
import com.abogomazov.merchant.guide.usecase.market.GetResourceMarketPriceUseCase

data class GetResourceMarketPriceCommand(
    private val galaxyNumber: GalaxyNumber,
    private val resource: Resource,
) : BusinessCommand {

    fun execute(usecase: GetResourceMarketPriceUseCase) =
        usecase.execute(resource, galaxyNumber)
            .fold({ it.toError() }, { response(it) })

    private fun response(result: Credits) = "$galaxyNumber $resource is ${result.toBigInteger()} Credits"

    private fun GetResourceMarketPriceError.toError() =
        when (this) {
            is GetResourceMarketPriceError.TranslationNotFound -> translationNotFound(this.digit)
            GetResourceMarketPriceError.RomanNotationRulesViolated -> romanNotationRulesViolated(galaxyNumber)
            GetResourceMarketPriceError.PriceNotFound -> resourcePriceNotFound(resource)
        }
}
