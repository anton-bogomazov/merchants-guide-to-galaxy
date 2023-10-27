package com.abogomazov.merchant.guide.integration

import com.abogomazov.merchant.guide.cli.ApplicationShell
import com.abogomazov.merchant.guide.cli.CommandSource
import com.abogomazov.merchant.guide.cli.ResultCollector
import com.abogomazov.merchant.guide.storage.inmemory.InMemoryDictionary
import com.abogomazov.merchant.guide.storage.inmemory.InMemoryMarket
import com.abogomazov.merchant.guide.usecase.common.GalaxyNumberEvaluator
import com.abogomazov.merchant.guide.usecase.market.GetResourceMarketPriceUseCase
import com.abogomazov.merchant.guide.usecase.market.SetResourceMarketPriceUseCase
import com.abogomazov.merchant.guide.usecase.translator.GetTranslationUseCase
import com.abogomazov.merchant.guide.usecase.translator.SetTranslationUseCase
import io.kotest.matchers.shouldBe

private object ApplicationDriver {

    fun run(commandProvider: UserInputStream, asserter: ResultCollector) {
        val dictionary = InMemoryDictionary()
        val market = InMemoryMarket()

        val evaluator = GalaxyNumberEvaluator(dictionary)
        val getTranslationUseCase = GetTranslationUseCase(evaluator)
        val setTranslationUseCase = SetTranslationUseCase(
            dictionary, dictionary, dictionary)
        val getPriceUseCase = GetResourceMarketPriceUseCase(evaluator, market)
        val setPriceUseCase = SetResourceMarketPriceUseCase(evaluator, market)

        ApplicationShell(
            setTranslationUseCase,
            getTranslationUseCase,
            setPriceUseCase,
            getPriceUseCase,
            commandSource = commandProvider,
            resultCollector = asserter
        ).run()
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
