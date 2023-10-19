package com.abogomazov.merchant.guide.application

import arrow.core.Either
import com.abogomazov.merchant.guide.cli.CommandExecutor
import com.abogomazov.merchant.guide.cli.commands.BusinessCommand
import com.abogomazov.merchant.guide.cli.commands.Command
import com.abogomazov.merchant.guide.cli.commands.ExitCommand
import com.abogomazov.merchant.guide.cli.commands.UnknownCommand
import com.abogomazov.merchant.guide.parser.ParserError


class ApplicationShell(
    private val commandParserFactory: CommandParserFactory,
    private val commandExecutor: CommandExecutor,
    private val commandSource: CommandSource,
    private val resultCollector: ResultCollector,
) {
    fun run() {
        do {
            val userInput = commandSource.read()
            val command = commandParserFactory.create(userInput).parse()
                .fold({ UnknownCommand }, { it })

            when (command) {
                is ExitCommand -> break
                is BusinessCommand -> resultCollector.push(commandExecutor.execute(command))
            }
        } while (true)
    }
}

fun interface CommandParserFactory {
    fun create(command: String): CommandParser
}

fun interface CommandParser {
    fun parse(): Either<ParserError, Command>
}

fun interface CommandSource {
    fun read(): String
}

fun interface ResultCollector {
    fun push(data: String)
}
