package com.abogomazov.merchant.guide.cli

import arrow.core.Either
import com.abogomazov.merchant.guide.application.Application
import com.abogomazov.merchant.guide.cli.commands.BusinessCommand
import com.abogomazov.merchant.guide.cli.commands.Command
import com.abogomazov.merchant.guide.cli.commands.ExitCommand
import com.abogomazov.merchant.guide.cli.commands.InvalidCommand
import com.abogomazov.merchant.guide.cli.commands.UnknownCommand
import com.abogomazov.merchant.guide.cli.parser.ParserError
import com.abogomazov.merchant.guide.usecase.market.GetResourceMarketPriceUseCase
import com.abogomazov.merchant.guide.usecase.market.SetResourceMarketPriceUseCase
import com.abogomazov.merchant.guide.usecase.translator.GetTranslationUseCase
import com.abogomazov.merchant.guide.usecase.translator.SetTranslationUseCase
import org.slf4j.LoggerFactory

class ApplicationShell(
    private val setTranslationUseCase: SetTranslationUseCase,
    private val getTranslationUseCase: GetTranslationUseCase,
    private val setPriceUseCase: SetResourceMarketPriceUseCase,
    private val getPriceUseCase: GetResourceMarketPriceUseCase,
    private val commandSource: CommandSource,
    private val resultCollector: ResultCollector,
) : Application {
    override fun run() {
        val parser = ParserFactory().create()
        val executor = CommandExecutor(
            getTranslationUseCase,
            setTranslationUseCase,
            setPriceUseCase,
            getPriceUseCase
        )

        do {
            val userInput = commandSource.read()
            logger.info("Processing input: \"$userInput\"")
            val command = parser.parse(userInput)
                .fold({ resolveErrors(it) }, { it })

            when (command) {
                is ExitCommand -> break
                is BusinessCommand -> {
                    logger.info("Running $command")
                    resultCollector.push(executor.execute(command))
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

interface CommandParser {
    fun parse(command: String): Either<ParserError, Command>
}

fun interface CommandSource {
    fun read(): String
}

fun interface ResultCollector {
    fun push(data: String)
}
