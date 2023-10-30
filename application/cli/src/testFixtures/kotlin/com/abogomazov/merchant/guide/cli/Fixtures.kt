package com.abogomazov.merchant.guide.cli

import arrow.core.Either
import arrow.core.left
import arrow.core.raise.either
import arrow.core.right
import com.abogomazov.merchant.guide.cli.commands.Command
import com.abogomazov.merchant.guide.cli.parser.ParserError
import com.abogomazov.merchant.guide.domain.market.Credits
import com.abogomazov.merchant.guide.domain.roman.Amount
import com.abogomazov.merchant.guide.usecase.market.GetResourceMarketPriceError
import com.abogomazov.merchant.guide.usecase.market.SetResourceMarketPriceError
import com.abogomazov.merchant.guide.usecase.translator.GetTranslationError
import com.abogomazov.merchant.guide.usecase.translator.SetTranslationError

object NullCommand : Command

object NullParser : CommandParser {
    override fun parse(command: String): Either<ParserError, Command> =
        NullCommand.right()
}
