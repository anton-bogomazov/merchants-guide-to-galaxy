package com.abogomazov.merchant.guide.cli.parser.translator

import arrow.core.Either
import arrow.core.left
import arrow.core.raise.either
import arrow.core.right
import com.abogomazov.merchant.guide.application.CommandParser
import com.abogomazov.merchant.guide.cli.commands.BusinessCommand
import com.abogomazov.merchant.guide.cli.commands.SetTranslationCommand
import com.abogomazov.merchant.guide.cli.parser.CommandArguments
import com.abogomazov.merchant.guide.cli.parser.CommandRegexBuilder
import com.abogomazov.merchant.guide.cli.parser.ParserError
import com.abogomazov.merchant.guide.cli.parser.toLocalDigit
import com.abogomazov.merchant.guide.cli.parser.toRomanDigit

class SetTranslationCommandParser(
    private val command: String
) : CommandParser {
    companion object {
        private val COMMAND_REGEX = CommandRegexBuilder()
            .LocalNum().s().iz().s().RomanNum().os()
            .build()

        fun match(command: String) = command.matches(COMMAND_REGEX)
    }

    override fun parse(): Either<ParserError, BusinessCommand> =
        extractArguments().map { (localNum, romanNum) ->
            return SetTranslationArguments(localNum, romanNum).toCommand()
        }

    private fun extractArguments() = either<ParserError.FailedToExtractArguments, Pair<String, String>> {
        val groups = COMMAND_REGEX.find(command)?.groups ?: return ParserError.FailedToExtractArguments.left()
        val localNum = groups[1]?.value ?: return ParserError.FailedToExtractArguments.left()
        val romanNum = groups[2]?.value ?: return ParserError.FailedToExtractArguments.left()

        return Pair(localNum, romanNum).right()
    }
}

data class SetTranslationArguments(
    private val localDigit: String,
    private val romanDigit: String,
): CommandArguments {
    override fun toCommand() = either<ParserError.InvalidArguments, BusinessCommand> {
        SetTranslationCommand(
            localDigit = localDigit.toLocalDigit(),
            romanDigit = romanDigit.toRomanDigit(),
        )
    }
}
