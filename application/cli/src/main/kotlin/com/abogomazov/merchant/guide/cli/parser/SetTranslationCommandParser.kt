package com.abogomazov.merchant.guide.cli.parser

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.raise.either
import arrow.core.right
import com.abogomazov.merchant.guide.cli.CommandParser
import com.abogomazov.merchant.guide.cli.commands.Command
import com.abogomazov.merchant.guide.cli.commands.SetTranslationCommand
import com.abogomazov.merchant.guide.cli.parser.utils.CommandRegexBuilder
import com.abogomazov.merchant.guide.cli.parser.utils.getTwoArguments
import com.abogomazov.merchant.guide.domain.galaxy.toGalaxyNumeral
import com.abogomazov.merchant.guide.domain.roman.toRomanNumeral

class SetTranslationCommandParser(
    next: CommandParser
) : RegexCommandParser(next, COMMAND_REGEX) {
    companion object {
        private val COMMAND_REGEX = CommandRegexBuilder()
            .os().GalaxyNum().s().iz().s().RomanNum().os()
            .build()

        private fun String.extractArguments() =
            COMMAND_REGEX.find(this).getTwoArguments()?.right()
                ?: ParserError.FailedToExtractArguments.left()
    }

    override fun constructCommand(command: String): Either<ParserError, Command> =
        command.extractArguments().flatMap { (galaxyNumeral, romanNumeral) ->
            either {
                Pair(
                    galaxyNumeral.toGalaxyNumeral().bind(),
                    romanNumeral.toRomanNumeral().bind()
                )
            }.map { (galaxyNumeral, romanNumeral) ->
                SetTranslationCommand(
                    galaxyNumeral = galaxyNumeral,
                    romanNumeral = romanNumeral,
                )
            }.mapLeft {
                ParserError.InvalidArguments
            }
        }
}
