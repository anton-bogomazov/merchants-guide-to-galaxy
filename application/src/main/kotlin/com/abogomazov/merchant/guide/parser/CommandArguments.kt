package com.abogomazov.merchant.guide.parser

import com.abogomazov.merchant.guide.cli.commands.Command

fun interface CommandArguments {
    fun toCommand(): Command
}
