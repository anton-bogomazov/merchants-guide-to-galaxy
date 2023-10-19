package com.abogomazov.merchant.guide.parser

import arrow.core.raise.either
import arrow.core.right
import com.abogomazov.merchant.guide.application.CommandParser
import com.abogomazov.merchant.guide.cli.commands.BusinessCommand
import com.abogomazov.merchant.guide.cli.commands.UnknownCommand

object UnknownCommandParser : CommandParser {
    override fun parse() = either<ParserError, BusinessCommand> {
        return UnknownCommand.right()
    }
}
