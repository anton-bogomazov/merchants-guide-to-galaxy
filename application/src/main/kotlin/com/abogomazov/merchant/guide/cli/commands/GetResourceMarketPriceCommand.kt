package com.abogomazov.merchant.guide.cli.commands

import com.abogomazov.merchant.guide.domain.local.LocalNumber
import com.abogomazov.merchant.guide.domain.market.Credits
import com.abogomazov.merchant.guide.domain.market.Resource
import com.abogomazov.merchant.guide.usecase.market.GetResourceMarketPriceUseCase
import com.abogomazov.merchant.guide.usecase.market.GetResourceMarketPriceUseCaseError

data class GetResourceMarketPriceCommand(
    private val localNum: LocalNumber,
    private val resource: Resource,
) : BusinessCommand {

    fun execute(usecase: GetResourceMarketPriceUseCase) =
        usecase.execute(localNum, resource)
            .fold({ it.toError() }, { response(it) })

    private fun response(result: Credits) = "$localNum $resource is ${result.toBigInteger()} Credits"

}

private fun GetResourceMarketPriceUseCaseError.toError() =
    when (this) {
        GetResourceMarketPriceUseCaseError.TranslationNotFound -> errorMessage(this)
        GetResourceMarketPriceUseCaseError.NumberIsNotFollowingRomanNotationRules -> errorMessage(this)
        GetResourceMarketPriceUseCaseError.PriceNotFound -> errorMessage(this)
    }
