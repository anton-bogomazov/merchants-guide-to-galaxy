package com.abogomazov.merchant.guide.cli.parser.translator

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.abogomazov.merchant.guide.cli.CommandParser
import com.abogomazov.merchant.guide.cli.commands.Command
import com.abogomazov.merchant.guide.cli.commands.GetTranslationCommand
import com.abogomazov.merchant.guide.cli.parser.ParserError
import com.abogomazov.merchant.guide.cli.parser.utils.CommandRegexBuilder
import com.abogomazov.merchant.guide.cli.parser.utils.getOneArgument
import com.abogomazov.merchant.guide.cli.parser.utils.toLocalNumber

class GetTranslationCommandParser(
    private val next: CommandParser
) : CommandParser {
    companion object {
        private val COMMAND_REGEX = CommandRegexBuilder()
            .how().s().much().s().iz().s().LocalNum().s().question().os()
            .build()
    }

    override fun parse(command: String): Either<ParserError, Command> =
        if (!command.matches(COMMAND_REGEX)) {
            next.parse(command)
        } else {
            command.extractArguments().map { localNum ->
                return GetTranslationCommand(
                    localNum = localNum.toLocalNumber(),
                ).right()
            }
        }

    private fun String.extractArguments() =
        COMMAND_REGEX.find(this).getOneArgument()?.right()
            ?: ParserError.FailedToExtractArguments.left()
}
