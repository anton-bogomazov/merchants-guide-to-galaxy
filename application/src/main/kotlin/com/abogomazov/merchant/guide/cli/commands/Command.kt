package com.abogomazov.merchant.guide.cli.commands

interface Command

sealed interface BusinessCommand : Command
sealed interface ErrorCommand : Command {
    fun toError(): String
}
