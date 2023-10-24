package com.abogomazov.merchant.guide.cli.parser.market

import arrow.core.Either
import arrow.core.left
import arrow.core.raise.either
import arrow.core.right
import com.abogomazov.merchant.guide.cli.CommandParser
import com.abogomazov.merchant.guide.cli.commands.Command
import com.abogomazov.merchant.guide.cli.commands.SetResourceMarketPriceCommand
import com.abogomazov.merchant.guide.cli.parser.ParserError
import com.abogomazov.merchant.guide.cli.parser.utils.CommandRegexBuilder
import com.abogomazov.merchant.guide.cli.parser.utils.getThreeArguments
import com.abogomazov.merchant.guide.cli.parser.utils.toCredit
import com.abogomazov.merchant.guide.cli.parser.utils.toLocalNumber
import com.abogomazov.merchant.guide.cli.parser.utils.toResource

class SetResourceMarketPriceCommandParser(private val next: CommandParser) : CommandParser {

    companion object {
        private val COMMAND_REGEX = CommandRegexBuilder()
            .LocalNum().s().Resource().s().iz().s().ArabNum().s().credits().os()
            .build()
    }

    override fun parse(command: String): Either<ParserError, Command> =
        if (!command.matches(COMMAND_REGEX)) {
            next.parse(command)
        } else {
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

    private fun String.extractArguments() =
        COMMAND_REGEX.find(this).getThreeArguments()?.right()
            ?: ParserError.FailedToExtractArguments.left()
}
