package com.abogomazov.merchant.guide.cli

import arrow.core.Either
import com.abogomazov.merchant.guide.application.Application
import com.abogomazov.merchant.guide.cli.commands.BusinessCommand
import com.abogomazov.merchant.guide.cli.commands.Command
import com.abogomazov.merchant.guide.cli.commands.ExitCommand
import com.abogomazov.merchant.guide.cli.commands.InvalidCommand
import com.abogomazov.merchant.guide.cli.commands.UnknownCommand
import com.abogomazov.merchant.guide.cli.parser.ParserError
import org.slf4j.LoggerFactory

class ApplicationShell(
    private val commandExecutor: CommandExecutor,
    private val commandSource: CommandSource,
    private val resultCollector: ResultCollector,
) : Application {
    override fun run() {
        val commandParser = ParserFactory().create()
        do {
            val userInput = commandSource.read()
            logger.info("Processing input: \"$userInput\"")
            val command = commandParser.parse(userInput)
                .fold({ resolveErrors(it) }, { it })

            when (command) {
                is ExitCommand -> break
                is BusinessCommand -> {
                    logger.info("Running $command")
                    resultCollector.push(commandExecutor.execute(command))
                }
            }
        } while (true)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ApplicationShell::class.java)
    }
}

fun resolveErrors(error: ParserError) =
    when (error) {
        is ParserError.InvalidArguments -> InvalidCommand(error)
        is ParserError.FailedToExtractArguments -> UnknownCommand
    }

fun interface CommandParserFactory {
    fun create(): CommandParser
}

interface CommandParser {
    fun parse(command: String): Either<ParserError, Command>
}

fun interface CommandSource {
    fun read(): String
}

fun interface ResultCollector {
    fun push(data: String)
}
