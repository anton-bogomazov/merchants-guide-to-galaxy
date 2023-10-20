package com.abogomazov.merchant.guide.cli.parser.market

import arrow.core.Either
import arrow.core.left
import arrow.core.raise.either
import arrow.core.right
import com.abogomazov.merchant.guide.cli.CommandParser
import com.abogomazov.merchant.guide.cli.commands.BusinessCommand
import com.abogomazov.merchant.guide.cli.commands.SetResourceMarketPriceCommand
import com.abogomazov.merchant.guide.cli.parser.ParserError
import com.abogomazov.merchant.guide.cli.parser.utils.CommandRegexBuilder
import com.abogomazov.merchant.guide.cli.parser.utils.getThreeArguments
import com.abogomazov.merchant.guide.cli.parser.utils.toCredit
import com.abogomazov.merchant.guide.cli.parser.utils.toLocalNumber
import com.abogomazov.merchant.guide.cli.parser.utils.toResource

class SetResourceMarketPriceCommandParser(
    private val command: String
) : CommandParser {
    companion object {
        private val COMMAND_REGEX = CommandRegexBuilder()
            .LocalNum().s().Resource().s().iz().s().ArabNum().s().credits().os()
            .build()

        fun match(command: String) = command.matches(COMMAND_REGEX)
    }

    override fun parse(): Either<ParserError, BusinessCommand> =
        extractArguments().map { (localNum, resource, totalCredits) ->
            return either { Pair(resource.toResource().bind(), totalCredits.toCredit().bind()) }
                .map { (resource, credits) ->
                    SetResourceMarketPriceCommand(
                        resourceAmount = localNum.toLocalNumber(),
                        resource = resource,
                        total = credits,
                    )
                }
        }

    private fun extractArguments() =
        COMMAND_REGEX.find(command).getThreeArguments()?.right()
            ?: ParserError.FailedToExtractArguments.left()
}
