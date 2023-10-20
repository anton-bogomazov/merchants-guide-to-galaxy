package com.abogomazov.merchant.guide.cli.parser

import arrow.core.Either
import arrow.core.right
import com.abogomazov.merchant.guide.cli.CommandParser
import com.abogomazov.merchant.guide.cli.commands.Command
import com.abogomazov.merchant.guide.cli.commands.ExitCommand

class ExitCommandExtractor : CommandParser {

    companion object {
        fun match(command: String) = command.trim().lowercase() == "exit"
    }

    override fun parse(): Either<ParserError, Command> {
        return ExitCommand.right()
    }
}
