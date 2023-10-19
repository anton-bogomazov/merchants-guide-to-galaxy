package com.abogomazov.merchant.guide.parser

import arrow.core.Either
import arrow.core.right
import com.abogomazov.merchant.guide.application.CommandParser
import com.abogomazov.merchant.guide.cli.commands.Command
import com.abogomazov.merchant.guide.cli.commands.ExitCommand

class ExitCommandExtractor(
    private val command: String
) : CommandParser {

    companion object {
        fun match(command: String) = command.trim().lowercase() == "exit"
    }

    override fun parse(): Either<ParserError, Command> {
        return ExitCommand.right()
    }
}