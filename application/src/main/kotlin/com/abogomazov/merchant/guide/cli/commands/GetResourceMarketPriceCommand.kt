package com.abogomazov.merchant.guide.cli.commands

import com.abogomazov.merchant.guide.domain.local.LocalNumber
import com.abogomazov.merchant.guide.domain.market.Credits
import com.abogomazov.merchant.guide.domain.market.Resource
import com.abogomazov.merchant.guide.usecase.market.GetResourceMarketPriceUseCase
import com.abogomazov.merchant.guide.usecase.market.GetResourceMarketPriceError

data class GetResourceMarketPriceCommand(
    private val localNum: LocalNumber,
    private val resource: Resource,
) : BusinessCommand {

    fun execute(usecase: GetResourceMarketPriceUseCase) =
        usecase.execute(localNum, resource)
            .fold({ it.toError() }, { response(it) })

    private fun response(result: Credits) = "$localNum $resource is ${result.toBigInteger()} Credits"

    private fun GetResourceMarketPriceError.toError() =
        when (this) {
            GetResourceMarketPriceError.TranslationNotFound -> translationNotFound(localNum)
            GetResourceMarketPriceError.RomanNotationRulesViolated -> romanNotationRulesViolated(localNum)
            GetResourceMarketPriceError.PriceNotFound -> resourcePriceNotFound(resource)
        }
}
