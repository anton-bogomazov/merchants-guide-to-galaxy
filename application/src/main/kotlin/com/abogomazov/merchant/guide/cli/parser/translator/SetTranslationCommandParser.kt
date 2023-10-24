package com.abogomazov.merchant.guide.cli.parser.translator

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.abogomazov.merchant.guide.cli.CommandParser
import com.abogomazov.merchant.guide.cli.commands.Command
import com.abogomazov.merchant.guide.cli.commands.SetTranslationCommand
import com.abogomazov.merchant.guide.cli.parser.ParserError
import com.abogomazov.merchant.guide.cli.parser.utils.CommandRegexBuilder
import com.abogomazov.merchant.guide.cli.parser.utils.getTwoArguments
import com.abogomazov.merchant.guide.cli.parser.utils.toLocalDigit
import com.abogomazov.merchant.guide.cli.parser.utils.toRomanDigit

class SetTranslationCommandParser(private val next: CommandParser) : CommandParser {
    companion object {
        private val COMMAND_REGEX = CommandRegexBuilder()
            .LocalNum().s().iz().s().RomanNum().os()
            .build()
    }

    override fun parse(command: String): Either<ParserError, Command> {
        return if (!command.matches(COMMAND_REGEX)) {
            next.parse(command)
        } else {
            command.extractArguments().map { (localDigit, romanDigit) ->
                return SetTranslationCommand(
                    localDigit = localDigit.toLocalDigit(),
                    romanDigit = romanDigit.toRomanDigit(),
                ).right()
            }
        }
    }

    private fun String.extractArguments() =
        COMMAND_REGEX.find(this).getTwoArguments()?.right()
            ?: ParserError.FailedToExtractArguments.left()
}
