package com.abogomazov.merchant.guide.cli.parser.market

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.abogomazov.merchant.guide.cli.CommandParser
import com.abogomazov.merchant.guide.cli.commands.Command
import com.abogomazov.merchant.guide.cli.commands.GetResourceMarketPriceCommand
import com.abogomazov.merchant.guide.cli.parser.ParserError
import com.abogomazov.merchant.guide.cli.parser.utils.CommandRegexBuilder
import com.abogomazov.merchant.guide.cli.parser.utils.getTwoArguments
import com.abogomazov.merchant.guide.cli.parser.utils.toLocalNumber
import com.abogomazov.merchant.guide.cli.parser.utils.toResource

class GetResourceMarketPriceCommandParser(
    private val next: CommandParser
) : CommandParser {
    companion object {
        private val COMMAND_REGEX = CommandRegexBuilder()
            .how().s().many().s().credits().s().iz().s().LocalNum().s().Resource().s().question().os()
            .build()
    }

    override fun parse(command: String): Either<ParserError, Command> =
        if (!command.matches(COMMAND_REGEX)) {
            next.parse(command)
        } else {
            command.extractArguments().map { (localNum, resource) ->
                return resource.toResource().map {
                    GetResourceMarketPriceCommand(
                        localNum = localNum.toLocalNumber(),
                        resource = it,
                    )
                }
            }
        }

    private fun String.extractArguments() =
        COMMAND_REGEX.find(this).getTwoArguments()?.right()
            ?: ParserError.FailedToExtractArguments.left()
}


