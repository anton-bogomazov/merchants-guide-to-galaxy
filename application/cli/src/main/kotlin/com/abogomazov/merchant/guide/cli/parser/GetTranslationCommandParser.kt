package com.abogomazov.merchant.guide.cli.parser

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.abogomazov.merchant.guide.cli.CommandParser
import com.abogomazov.merchant.guide.cli.commands.Command
import com.abogomazov.merchant.guide.cli.commands.GetTranslationCommand
import com.abogomazov.merchant.guide.cli.parser.utils.CommandRegexBuilder
import com.abogomazov.merchant.guide.cli.parser.utils.getOneArgument
import com.abogomazov.merchant.guide.domain.galaxy.toGalaxyNumber

class GetTranslationCommandParser(
    next: CommandParser
) : RegexCommandParser(next, COMMAND_REGEX) {
    companion object {
        private val COMMAND_REGEX = CommandRegexBuilder()
            .os().how().s().much().s().iz().s().GalaxyNum().s().question().os()
            .build()

        private fun String.extractArguments() =
            COMMAND_REGEX.find(this).getOneArgument()?.right()
                ?: ParserError.FailedToExtractArguments.left()
    }

    override fun constructCommand(command: String): Either<ParserError, Command> =
        command.extractArguments().flatMap { galaxyNumber ->
            galaxyNumber.toGalaxyNumber().map {
                GetTranslationCommand(galaxyNumber = it)
            }.mapLeft { ParserError.InvalidArguments }
        }
}
