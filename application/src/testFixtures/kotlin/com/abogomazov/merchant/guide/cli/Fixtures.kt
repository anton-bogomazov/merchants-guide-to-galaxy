package com.abogomazov.merchant.guide.cli

import arrow.core.Either
import arrow.core.right
import com.abogomazov.merchant.guide.cli.commands.Command
import com.abogomazov.merchant.guide.cli.parser.ParserError

object NullCommand : Command

object NullParser : CommandParser {
    override fun parse(command: String): Either<ParserError, Command> =
        NullCommand.right()
}
