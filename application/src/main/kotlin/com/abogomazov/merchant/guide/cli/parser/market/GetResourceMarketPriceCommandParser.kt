package com.abogomazov.merchant.guide.cli.parser.market

import arrow.core.Either
import arrow.core.left
import arrow.core.raise.either
import arrow.core.right
import com.abogomazov.merchant.guide.cli.CommandParser
import com.abogomazov.merchant.guide.cli.commands.BusinessCommand
import com.abogomazov.merchant.guide.cli.commands.GetResourceMarketPriceCommand
import com.abogomazov.merchant.guide.cli.parser.CommandArguments
import com.abogomazov.merchant.guide.cli.parser.CommandRegexBuilder
import com.abogomazov.merchant.guide.cli.parser.ParserError
import com.abogomazov.merchant.guide.cli.parser.toLocalNumber
import com.abogomazov.merchant.guide.cli.parser.toResource

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
            return GetMarketPriceArguments(
                localNum,
                resource
            ).toCommand()
        }

    private fun extractArguments() = either<ParserError.FailedToExtractArguments, Pair<String, String>> {
        val groups = COMMAND_REGEX.find(command)?.groups ?: return ParserError.FailedToExtractArguments.left()
        val localNum = groups[1]?.value ?: return ParserError.FailedToExtractArguments.left()
        val resource = groups[2]?.value ?: return ParserError.FailedToExtractArguments.left()

        return Pair(localNum, resource).right()
    }

}

private data class GetMarketPriceArguments(
    private val localNum: String,
    private val resource: String,
): CommandArguments {
    override fun toCommand() = resource.toResource().map {
        GetResourceMarketPriceCommand(
            localNum = localNum.toLocalNumber(),
            resource = it,
        )
    }
}
