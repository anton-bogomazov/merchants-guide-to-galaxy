package com.abogomazov.merchant.guide.integration

import com.abogomazov.merchant.guide.application.Application
import com.abogomazov.merchant.guide.cli.CommandSource
import com.abogomazov.merchant.guide.cli.ParserFactory
import com.abogomazov.merchant.guide.cli.ResultCollector
import com.abogomazov.merchant.guide.application.inmemory.InMemoryMarket
import com.abogomazov.merchant.guide.application.inmemory.InMemoryDictionary
import io.kotest.matchers.shouldBe

private object ApplicationDriver {

    fun run(commandProvider: UserInputStream, asserter: ResultCollector) {
        val dictionary = InMemoryDictionary()
        val market = InMemoryMarket()
        val parserFactory = ParserFactory()

        Application(
            translationPersister = dictionary,
            translationProvider = dictionary,
            translationRemover = dictionary,
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

private class UserInputStream(
    commands: List<String>
) : CommandSource {

    private val inputs = commands.toMutableList()

    override fun read(): String {
        return inputs.removeFirstOrNull() ?: "exit"
    }

}

private class Asserter(
    outputs: List<String>
) : ResultCollector {

    private val outputs = outputs.toMutableList()

    override fun push(data: String) {
        val expected = outputs.removeFirstOrNull() ?: return
        data shouldBe expected
    }
}

fun setDictionary() = listOf(
    "glob is I" to "Set",
    "prok is V" to "Set",
    "prok is V" to "Set",
    "pish is X" to "Set",
    "tegj is L" to "Set",
    "boop is C" to "Set",
    "whoop is D" to "Set",
    "groop is M" to "Set",
)
