package com.abogomazov.merchant.guide.cli.commands

import com.abogomazov.merchant.guide.cli.parser.ParserError

data class InvalidCommand(val reason: ParserError): BusinessCommand {
    fun message() = errorMessage(reason)
}
