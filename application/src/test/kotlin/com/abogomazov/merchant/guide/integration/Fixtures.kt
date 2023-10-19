package com.abogomazov.merchant.guide.integration

import com.abogomazov.merchant.guide.application.Application
import com.abogomazov.merchant.guide.application.InputReader
import com.abogomazov.merchant.guide.application.ParserFactory
import com.abogomazov.merchant.guide.application.Printer
import com.abogomazov.merchant.guide.application.inmemory.InMemoryMarket
import com.abogomazov.merchant.guide.application.inmemory.InMemoryTranslationRegistry
import io.kotest.matchers.shouldBe

object ApplicationDriver {

    fun run(commandProvider: UserInputStream, asserter: Printer) {
        val dictionary = InMemoryTranslationRegistry()
        val market = InMemoryMarket()
        val parserFactory = ParserFactory()

        Application(
            translationPersister = dictionary,
            translationProvider = dictionary,
            marketPricePersister = market,
            marketPriceProvider = market,
            printer = asserter,
            inputReader = commandProvider,
            parserFactory = parserFactory
        ).build().run()
    }

}

fun runTest(inputs: List<String>, expectedOutputs: List<String>) {
    ApplicationDriver.run(
        UserInputStream(inputs),
        Asserter(expectedOutputs)
    )
}

class UserInputStream(
    commands: List<String>
) : InputReader {

    private val inputs = commands.toMutableList()

    override fun read(): String {
        return inputs.removeFirstOrNull() ?: "exit"
    }

}

class Asserter(
    outputs: List<String>
) : Printer {

    private val outputs = outputs.toMutableList()

    override fun print(data: String) {
        data shouldBe outputs.removeFirstOrNull()
    }
}
