package com.abogomazov.merchant.guide.cli.parser.market

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.abogomazov.merchant.guide.cli.CommandParser
import com.abogomazov.merchant.guide.cli.commands.BusinessCommand
import com.abogomazov.merchant.guide.cli.commands.GetResourceMarketPriceCommand
import com.abogomazov.merchant.guide.cli.parser.ParserError
import com.abogomazov.merchant.guide.cli.parser.utils.CommandRegexBuilder
import com.abogomazov.merchant.guide.cli.parser.utils.getTwoArguments
import com.abogomazov.merchant.guide.cli.parser.utils.toLocalNumber
import com.abogomazov.merchant.guide.cli.parser.utils.toResource

class GetResourceMarketPriceCommandParser(
    private val command: String
) : CommandParser {
    companion object {
        private val COMMAND_REGEX = CommandRegexBuilder()
            .how().s().many().s().credits().s().iz().s().LocalNum().s().Resource().s().question().os()
            .build()

        fun match(command: String) = command.matches(COMMAND_REGEX)
    }

    override fun parse(): Either<ParserError, BusinessCommand> =
        extractArguments().map { (localNum, resource) ->
            return resource.toResource().map {
                GetResourceMarketPriceCommand(
                    localNum = localNum.toLocalNumber(),
                    resource = it,
                )
            }
        }

    private fun extractArguments() =
        COMMAND_REGEX.find(command).getTwoArguments()?.right()
            ?: ParserError.FailedToExtractArguments.left()
}
