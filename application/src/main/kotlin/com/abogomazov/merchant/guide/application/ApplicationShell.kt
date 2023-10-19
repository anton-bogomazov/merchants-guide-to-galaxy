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
    private val reader: InputReader,
    private val printer: Printer,
) {
    fun run() {
        do {
            val userInput = reader.read()
            val command = commandParserFactory.create(userInput).parse()
                .fold({ UnknownCommand }, { it })

            when (command) {
                is ExitCommand -> break
                is BusinessCommand -> printer.print(commandExecutor.execute(command))
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

fun interface InputReader {
    fun read(): String
}

fun interface Printer {
    fun print(data: String)
}
