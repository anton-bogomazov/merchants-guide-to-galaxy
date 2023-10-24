package com.abogomazov.merchant.guide.cli.parser

import arrow.core.Either
import arrow.core.right
import com.abogomazov.merchant.guide.cli.CommandParser
import com.abogomazov.merchant.guide.cli.commands.Command
import com.abogomazov.merchant.guide.cli.commands.ExitCommand

class ExitCommandParser(
    private val next: CommandParser
) : CommandParser {
    override fun parse(command: String): Either<ParserError, Command> {
        return if (command.trim().lowercase() != "exit") {
            next.parse(command)
        } else {
            ExitCommand.right()
        }
    }
}
