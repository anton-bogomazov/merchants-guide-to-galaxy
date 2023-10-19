package com.abogomazov.merchant.guide.cli.commands

interface Command

sealed interface BusinessCommand : Command

data object UnknownCommand : BusinessCommand {
    const val ERROR_MESSAGE = "I have no idea what you are talking about"
}
