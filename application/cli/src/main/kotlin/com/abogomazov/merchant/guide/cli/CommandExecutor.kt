package com.abogomazov.merchant.guide.cli

import com.abogomazov.merchant.guide.cli.commands.BusinessCommand
import com.abogomazov.merchant.guide.cli.commands.ErrorCommand
import com.abogomazov.merchant.guide.cli.commands.GetResourceMarketPriceCommand
import com.abogomazov.merchant.guide.cli.commands.GetTranslationCommand
import com.abogomazov.merchant.guide.cli.commands.SetResourceMarketPriceCommand
import com.abogomazov.merchant.guide.cli.commands.SetTranslationCommand
import com.abogomazov.merchant.guide.usecase.market.GetResourceMarketPriceUseCase
import com.abogomazov.merchant.guide.usecase.market.SetResourceMarketPriceUseCase
import com.abogomazov.merchant.guide.usecase.translator.GetTranslationUseCase
import com.abogomazov.merchant.guide.usecase.translator.SetTranslationUseCase

class CommandExecutor(
    private val getTranslation: GetTranslationUseCase,
    private val setTranslation: SetTranslationUseCase,
    private val setPrice: SetResourceMarketPriceUseCase,
    private val getPrice: GetResourceMarketPriceUseCase,
) {

    fun execute(businessCommand: BusinessCommand): String =
        when (businessCommand) {
            is GetTranslationCommand -> businessCommand.execute(getTranslation)
            is SetTranslationCommand -> businessCommand.execute(setTranslation)
            is GetResourceMarketPriceCommand -> businessCommand.execute(getPrice)
            is SetResourceMarketPriceCommand -> businessCommand.execute(setPrice)
            is ErrorCommand -> businessCommand.toError()
        }
}
