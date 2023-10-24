package com.abogomazov.merchant.guide.cli.parser

import arrow.core.right
import com.abogomazov.merchant.guide.cli.CommandParser
import com.abogomazov.merchant.guide.cli.commands.UnknownCommand

object UnknownCommandParser : CommandParser {
    override fun parse(command: String) = UnknownCommand.right()
}
