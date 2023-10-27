package com.abogomazov.merchant.guide.cli.commands

import com.abogomazov.merchant.guide.cli.parser.ParserError

data class InvalidCommand(
    private val reason: ParserError
) : BusinessCommand, ErrorCommand {
    override fun toError() = parsingFailed(reason)
}
