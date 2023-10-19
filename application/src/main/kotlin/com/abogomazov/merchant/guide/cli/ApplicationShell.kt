package com.abogomazov.merchant.guide.cli

import com.abogomazov.merchant.guide.cli.commands.Command


class ApplicationShell(
    private val argumentExtractorFactory: ArgumentExtractorFactory,
    private val commandExecutor: CommandExecutor,
    private val reader: InputReader,
    private val printer: Printer,
) {
    fun run() {
        do {
            val userInput = reader.read()
            val command = argumentExtractorFactory.create(userInput).extract().toCommand()
            val response = commandExecutor.execute(command)
            printer.print(response)
        } while (true)
    }
}

fun interface ArgumentExtractorFactory {
    fun create(command: String): ArgumentExtractor
}

fun interface CommandArguments {
    fun toCommand(): Command
}

fun interface ArgumentExtractor {
    fun extract(): CommandArguments
}

fun interface InputReader {
    fun read(): String
}

fun interface Printer {
    fun print(data: String)
}
