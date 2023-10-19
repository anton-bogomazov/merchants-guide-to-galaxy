package com.abogomazov.merchant.guide.integration

import com.abogomazov.merchant.guide.application.Application
import com.abogomazov.merchant.guide.application.CommandSource
import com.abogomazov.merchant.guide.application.ParserFactory
import com.abogomazov.merchant.guide.application.ResultCollector
import com.abogomazov.merchant.guide.application.inmemory.InMemoryMarket
import com.abogomazov.merchant.guide.application.inmemory.InMemoryTranslationRegistry
import io.kotest.matchers.shouldBe

object ApplicationDriver {

    fun run(commandProvider: UserInputStream, asserter: ResultCollector) {
        val dictionary = InMemoryTranslationRegistry()
        val market = InMemoryMarket()
        val parserFactory = ParserFactory()

        Application(
            translationPersister = dictionary,
            translationProvider = dictionary,
            marketPricePersister = market,
            marketPriceProvider = market,
            resultCollector = asserter,
            commandSource = commandProvider,
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
) : CommandSource {

    private val inputs = commands.toMutableList()

    override fun read(): String {
        return inputs.removeFirstOrNull() ?: "exit"
    }

}

class Asserter(
    outputs: List<String>
) : ResultCollector {

    private val outputs = outputs.toMutableList()

    override fun push(data: String) {
        data shouldBe outputs.removeFirstOrNull()
    }
}
