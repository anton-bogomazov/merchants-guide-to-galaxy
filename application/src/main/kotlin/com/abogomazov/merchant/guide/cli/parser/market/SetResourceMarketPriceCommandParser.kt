package com.abogomazov.merchant.guide.cli.parser.market

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.abogomazov.merchant.guide.cli.CommandParser
import com.abogomazov.merchant.guide.cli.commands.BusinessCommand
import com.abogomazov.merchant.guide.cli.commands.SetResourceMarketPriceCommand
import com.abogomazov.merchant.guide.domain.market.Credits
import com.abogomazov.merchant.guide.cli.parser.utils.CommandRegexBuilder
import com.abogomazov.merchant.guide.cli.parser.ParserError
import com.abogomazov.merchant.guide.cli.parser.utils.getThreeArguments
import com.abogomazov.merchant.guide.cli.parser.utils.toLocalNumber
import com.abogomazov.merchant.guide.cli.parser.utils.toResource
import java.math.BigInteger

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
            return resource.toResource().map {
                SetResourceMarketPriceCommand(
                    resourceAmount = localNum.toLocalNumber(),
                    resource = it,
                    // TODO wrap validation error with Either
                    total = Credits(BigInteger.valueOf(totalCredits.toLong())),
                )
            }
        }

    private fun extractArguments() =
        COMMAND_REGEX.find(command).getThreeArguments()?.right()
            ?: ParserError.FailedToExtractArguments.left()
}
