package com.abogomazov.merchant.guide.cli.commands

data object UnknownCommand : BusinessCommand, ErrorCommand {
    private const val ERROR_MESSAGE = "I have no idea what you are talking about"

    override fun toError() = ERROR_MESSAGE
}
