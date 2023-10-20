package com.abogomazov.merchant.guide.cli.parser.translator

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.abogomazov.merchant.guide.cli.CommandParser
import com.abogomazov.merchant.guide.cli.commands.BusinessCommand
import com.abogomazov.merchant.guide.cli.commands.GetTranslationCommand
import com.abogomazov.merchant.guide.cli.parser.ParserError
import com.abogomazov.merchant.guide.cli.parser.utils.CommandRegexBuilder
import com.abogomazov.merchant.guide.cli.parser.utils.getOneArgument
import com.abogomazov.merchant.guide.cli.parser.utils.toLocalNumber

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

    private fun extractArguments() =
        COMMAND_REGEX.find(command).getOneArgument()?.right()
            ?: ParserError.FailedToExtractArguments.left()
}
