package com.abogomazov.merchant.guide.cli

import com.abogomazov.merchant.guide.cli.commands.Command
import com.abogomazov.merchant.guide.cli.commands.GetResourceMarketPriceCommand
import com.abogomazov.merchant.guide.cli.commands.SetResourceMarketPriceCommand
import com.abogomazov.merchant.guide.cli.commands.GetTranslationCommand
import com.abogomazov.merchant.guide.cli.commands.SetTranslationCommand
import com.abogomazov.merchant.guide.cli.commands.UnknownCommand
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

    fun execute(command: Command): String =
        when (command) {
            is GetTranslationCommand -> command.execute(getTranslation)
            is SetTranslationCommand -> command.execute(setTranslation)
            is GetResourceMarketPriceCommand -> command.execute(getPrice)
            is SetResourceMarketPriceCommand -> command.execute(setPrice)
            is UnknownCommand -> command.ERROR_MESSAGE
        }

}
