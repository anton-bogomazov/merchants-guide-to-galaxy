package com.abogomazov.merchant.guide.cli.commands

sealed interface Command

data object UnknownCommand : Command {
    const val ERROR_MESSAGE = "I have no idea what you are talking about"
}
