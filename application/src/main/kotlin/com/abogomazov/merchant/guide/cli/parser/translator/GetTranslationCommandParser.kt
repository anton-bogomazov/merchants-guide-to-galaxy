package com.abogomazov.merchant.guide.cli.parser.translator

import arrow.core.Either
import arrow.core.left
import arrow.core.raise.either
import arrow.core.right
import com.abogomazov.merchant.guide.cli.commands.BusinessCommand
import com.abogomazov.merchant.guide.cli.commands.GetTranslationCommand
import com.abogomazov.merchant.guide.cli.CommandParser
import com.abogomazov.merchant.guide.cli.parser.CommandRegexBuilder
import com.abogomazov.merchant.guide.cli.parser.ParserError
import com.abogomazov.merchant.guide.cli.parser.toLocalNumber

class GetTranslationCommandParser(
    private val command: String
) : CommandParser {
    companion object {
        private val COMMAND_REGEX = CommandRegexBuilder()
            .how().s().much().s().iz().s().LocalNum().s().question().os()
            .build()

        fun match(command: String) = command.matches(COMMAND_REGEX)
    }

    override fun parse(): Either<ParserError, BusinessCommand> =
        extractArguments().map { localNum ->
            return GetTranslationCommand(
                localNum = localNum.toLocalNumber(),
            ).right()
        }

    private fun extractArguments() = either<ParserError.FailedToExtractArguments, String> {
        val groups = COMMAND_REGEX.find(command)?.groups ?: return ParserError.FailedToExtractArguments.left()
        val localNum = groups[1]?.value ?: return ParserError.FailedToExtractArguments.left()

        return localNum.right()
    }
}
