package com.abogomazov.merchant.guide.cli.parser

import arrow.core.Either
import arrow.core.left
import arrow.core.raise.either
import arrow.core.right
import com.abogomazov.merchant.guide.cli.CommandParser
import com.abogomazov.merchant.guide.cli.commands.Command
import com.abogomazov.merchant.guide.cli.commands.SetResourceMarketPriceCommand
import com.abogomazov.merchant.guide.cli.parser.utils.CommandRegexBuilder
import com.abogomazov.merchant.guide.cli.parser.utils.getThreeArguments

class SetResourcePriceCommandParser(
    next: CommandParser
) : RegexCommandParser(next, COMMAND_REGEX) {
    companion object {
        private val COMMAND_REGEX = CommandRegexBuilder()
            .os().LocalNum().s().Resource().s().iz().s().ArabNum().s().credits().os()
            .build()

        private fun String.extractArguments() =
            COMMAND_REGEX.find(this).getThreeArguments()?.right()
                ?: ParserError.FailedToExtractArguments.left()
    }

    override fun constructCommand(command: String): Either<ParserError, Command> =
        command.extractArguments().map { (localNum, resource, totalCredits) ->
            return either { Pair(resource.toResource().bind(), totalCredits.toCredit().bind()) }
                .map { (resource, credits) ->
                    SetResourceMarketPriceCommand(
                        resourceAmount = localNum.toLocalNumber(),
                        resource = resource,
                        total = credits,
                    )
                }
        }
}
