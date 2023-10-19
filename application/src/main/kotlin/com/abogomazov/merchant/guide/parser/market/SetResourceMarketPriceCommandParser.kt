package com.abogomazov.merchant.guide.parser.market

import arrow.core.Either
import arrow.core.left
import arrow.core.raise.either
import arrow.core.right
import com.abogomazov.merchant.guide.application.CommandParser
import com.abogomazov.merchant.guide.cli.commands.Command
import com.abogomazov.merchant.guide.cli.commands.SetResourceMarketPriceCommand
import com.abogomazov.merchant.guide.domain.market.Credits
import com.abogomazov.merchant.guide.parser.CommandArguments
import com.abogomazov.merchant.guide.parser.CommandRegexBuilder
import com.abogomazov.merchant.guide.parser.ParserError
import com.abogomazov.merchant.guide.parser.toLocalNumber
import com.abogomazov.merchant.guide.parser.toResource
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

    override fun parse(): Either<ParserError, Command> =
        extractArguments().map { (localNum, resource, resourceAmount) ->
            return SetMarketPriceArguments(localNum, resource, resourceAmount).toCommand()
        }

    private fun extractArguments() = either<ParserError.FailedToExtractArguments, Triple<String, String, String>> {
        val groups = COMMAND_REGEX.find(command)?.groups ?: return ParserError.FailedToExtractArguments.left()
        val localNum = groups[1]?.value ?: return ParserError.FailedToExtractArguments.left()
        val resource = groups[2]?.value ?: return ParserError.FailedToExtractArguments.left()
        val resourceAmount = groups[3]?.value ?: return ParserError.FailedToExtractArguments.left()

        return Triple(localNum, resource, resourceAmount).right()
    }
}

data class SetMarketPriceArguments(
    private val localNum: String,
    private val resource: String,
    private val totalCredits: String,
): CommandArguments {
    override fun toCommand() = resource.toResource().map {
        SetResourceMarketPriceCommand(
            resourceAmount = localNum.toLocalNumber(),
            resource = it,
            // TODO wrap validation error with Either
            total = Credits(BigInteger.valueOf(totalCredits.toLong())),
        )
    }
}
