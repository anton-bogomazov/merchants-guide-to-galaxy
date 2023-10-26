package com.abogomazov.merchant.guide.cli.parser

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.abogomazov.merchant.guide.cli.CommandParser
import com.abogomazov.merchant.guide.cli.commands.Command
import com.abogomazov.merchant.guide.cli.commands.GetResourceMarketPriceCommand
import com.abogomazov.merchant.guide.cli.parser.utils.CommandRegexBuilder
import com.abogomazov.merchant.guide.cli.parser.utils.getTwoArguments

class GetResourcePriceCommandParser(
    next: CommandParser
) : RegexCommandParser(next, COMMAND_REGEX) {
    companion object {
        private val COMMAND_REGEX = CommandRegexBuilder()
            .os().how().s().many().s().credits().s().iz().s().LocalNum().s().Resource().s().question().os()
            .build()

        private fun String.extractArguments() =
            COMMAND_REGEX.find(this).getTwoArguments()?.right()
                ?: ParserError.FailedToExtractArguments.left()
    }

    override fun constructCommand(command: String): Either<ParserError, Command> =
        command.extractArguments().map { (localNum, resource) ->
            return resource.toResource().map {
                GetResourceMarketPriceCommand(
                    localNum = localNum.toLocalNumber(),
                    resource = it,
                )
            }
        }
}
