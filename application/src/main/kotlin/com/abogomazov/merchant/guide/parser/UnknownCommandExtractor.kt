package com.abogomazov.merchant.guide.parser

import arrow.core.raise.either
import arrow.core.right
import com.abogomazov.merchant.guide.application.CommandParser
import com.abogomazov.merchant.guide.cli.commands.Command
import com.abogomazov.merchant.guide.cli.commands.UnknownCommand

object UnknownCommandExtractor : CommandParser {
    override fun parse() = either<ParserError, Command> {
        return UnknownCommand.right()
    }
}
