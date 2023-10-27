package com.abogomazov.merchant.guide.cli.parser

import arrow.core.Either
import com.abogomazov.merchant.guide.cli.CommandParser
import com.abogomazov.merchant.guide.cli.commands.Command

abstract class RegexCommandParser(
    private val next: CommandParser,
    private val regex: Regex
) : CommandParser {

    override fun parse(command: String): Either<ParserError, Command> =
        if (!command.matches(regex)) {
            next.parse(command)
        } else {
            constructCommand(command)
        }

    protected abstract fun constructCommand(command: String): Either<ParserError, Command>
}
